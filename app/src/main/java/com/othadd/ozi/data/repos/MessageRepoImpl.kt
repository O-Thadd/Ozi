package com.othadd.ozi.data.repos

import android.content.Context
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.google.gson.Gson
import com.othadd.ozi.common.MAX_PAGE_SIZE_PER_REQUEST
import com.othadd.ozi.common.WORKER_MESSAGE_KEY
import com.othadd.ozi.data.dataSources.database.daos.MessageDao
import com.othadd.ozi.data.dataSources.localStore.DefaultOziDataStore
import com.othadd.ozi.data.dataSources.remote.OziRemoteService
import com.othadd.ozi.domain.model.message.toMessage
import com.othadd.ozi.domain.repos.MessageRepo
import com.othadd.ozi.domain.workers.SendChatMessageWorker
import com.othadd.ozi.domain.model.message.Message
import com.othadd.ozi.common.messageToString
import com.othadd.ozi.data.dataSources.localStore.OziDataStore
import com.othadd.ozi.domain.model.DataSourcePreference
import com.othadd.ozi.domain.model.OperationOutcome
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class MessageRepoImpl @Inject constructor(
    private val messageDao: MessageDao,
    private val context: Context,
    private val remoteService: OziRemoteService,
    private val dataStore: OziDataStore
) : MessageRepo {

    override suspend fun addMessage(message: Message) {
        messageDao.insert(message)
    }

    override suspend fun sendMessage(message: Message) {
        messageDao.insert(message)
        scheduleMessageForSendToServer(message)
    }

    override suspend fun postMessage(message: Message) {
        try {
            val token = dataStore.getThisUserFlow().first()!!.token!!
            remoteService.sendMessage(Gson().toJson(message), token)
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getMessages(chatId: String): Flow<List<Message>> {
        return messageDao.getMessagesByChatId(chatId)
    }

    override suspend fun getAllMessages(): Flow<List<Message>> {
        return messageDao.getAllMessages()
    }

    override suspend fun deleteAll() {
        messageDao.deleteAll()
    }


    override suspend fun syncMessages(
        chatId: String,
        lastMessageTimestamp: Long?,
        lastMessageId: String?
    ): List<Message> {
        val token = dataStore.getThisUserFlow().first()!!.token!!
        val fetchedMessages = remoteService.getMessages(
            chatId = chatId,
            lastMessageTimestamp = lastMessageTimestamp,
            lastMessageId = lastMessageId,
            token = token
        ).map { it.toMessage() }
        val currentMessages = messageDao.getMessagesByChatId(chatId).first()
        val result =
            processMessages(currentMessages, fetchedMessages, lastMessageTimestamp, lastMessageId)

        val deletedMessages = result.first
        val newAndUpdatedMessages = result.second
        val newMessages = result.third

        messageDao.deleteMultiple(deletedMessages)
        messageDao.insertMultiple(newAndUpdatedMessages)
        return newMessages
    }

    /**
     * Parameter [dataSourcePreference] only accepts [DataSourcePreference.LOCAL] and [DataSourcePreference.REMOTE].
     * Exception will be thrown otherwise.
     */
    override suspend fun fetchMessages(
        chatId: String,
        dataSourcePreference: DataSourcePreference,
        lastMessageTimestamp: Long?,
        lastMessageId: String?
    ): OperationOutcome<List<Message>, Nothing> {
        return when(dataSourcePreference) {
            DataSourcePreference.REMOTE -> {
                try {
                    val token = dataStore.getThisUserFlow().first()!!.token!!
                    val fetchedMessages = remoteService.getMessages(
                        chatId = chatId,
                        lastMessageTimestamp = lastMessageTimestamp,
                        lastMessageId = lastMessageId,
                        token = token
                    ).map { it.toMessage() }
                    OperationOutcome.Successful(fetchedMessages)
                } catch (e: Exception) {
                    OperationOutcome.Failed()
                }
            }

            DataSourcePreference.LOCAL -> {
                val fetchedMessages = messageDao.getMessagesByChatId(chatId).first()
                OperationOutcome.Successful(fetchedMessages)
            }

            DataSourcePreference.LOCAL_FIRST -> {
                throw Exception("LOCAL_FIRST source type not allowed here")
            }
        }
    }

    /**
     * Analyzes new messages with current messages to determine messages to be added, deleted and updated.
     * Returns a [Triple] where [Triple.first], [Triple.second], [Triple.third] are
     * messages to delete, messages to update and brand new messages respectively.
     */
    private fun processMessages(
        currentMessages: List<Message>,
        newMessages: List<Message>,
        lastMessageTimestamp: Long? = null,
        lastMessageId: String? = null
    ): Triple<List<Message>, List<Message>, List<Message>> {
        val messagesInRange = getMessagesInRange(
            currentMessages,
            newMessages,
            lastMessageTimestamp,
            lastMessageId
        )
        val messagesToAdd = mutableListOf<Message>()
        val messagesToDelete = mutableListOf<Message>()
        val brandNewMessages = mutableListOf<Message>()

        for (newMessage in newMessages) {
            val oldVersionOfNewMessage = currentMessages.find { it.id == newMessage.id }

            if (oldVersionOfNewMessage == null) {
                //message did not previously exist, this is a brand new message.
                messagesToAdd.add(newMessage)
                brandNewMessages.add(newMessage)
                continue
            }

            if (oldVersionOfNewMessage != newMessage) {
                //message exists, but differs from new. so update.
                messagesToDelete.add(oldVersionOfNewMessage)
                messagesToAdd.add(newMessage)
            }

            //remove old version of new message from 'messagesInRange'.
            //messages left in 'messageInRange' after iteration will be deleted.
            messagesInRange.remove(oldVersionOfNewMessage)
        }
        //messages remaining in 'messagesInRange' were not found among the received messages.
        //they've been deleted on the server. so delete locally as well.
        messagesToDelete.addAll(messagesInRange)

        return Triple(messagesToDelete, messagesToAdd, brandNewMessages)
    }


    private fun getMessagesInRange(
        messages: List<Message>,
        newMessages: List<Message>,
        lastMessageTimestamp: Long?,
        lastMessageId: String?
    ): MutableList<Message> {

        //this sorting maybe redundant. server expected to have it already sorted
        val newMessagesSorted =
            newMessages.sortedWith(compareByDescending<Message> { it.timestamp }.thenBy { it.id })
        val lastOfNewMessages = newMessagesSorted.last()

        return when {

            // when it is the only page
            lastMessageTimestamp == null && newMessagesSorted.size < MAX_PAGE_SIZE_PER_REQUEST -> {
                messages.toMutableList()
            }

            // when it is the last page
            lastMessageTimestamp != null && newMessagesSorted.size < MAX_PAGE_SIZE_PER_REQUEST -> {
                messages.filter { it.timestamp <= lastMessageTimestamp }.toMutableList()
                    .apply { removeAll { it.timestamp == lastMessageTimestamp && it.id <= lastMessageId!! } }
            }

            // when it is the first of multiple pages
            lastMessageTimestamp == null && newMessagesSorted.size == MAX_PAGE_SIZE_PER_REQUEST -> {
                messages.filter { it.timestamp >= lastOfNewMessages.timestamp }.toMutableList()
                    .apply { removeAll { it.timestamp == lastOfNewMessages.timestamp && it.id > lastOfNewMessages.id } }
            }

            // when it is any -except the first and last- of multiple pages
            else -> {
                messages.filter { it.timestamp <= lastMessageTimestamp!! && it.timestamp >= lastOfNewMessages.timestamp }
                    .toMutableList()
                    .apply { removeAll { (it.timestamp == lastMessageTimestamp && it.id <= lastMessageId!!) || (it.timestamp == lastOfNewMessages.timestamp && it.id > lastOfNewMessages.id) } }
            }
        }
    }

    private fun scheduleMessageForSendToServer(newMessage: Message) {
        val newMessageString = messageToString(newMessage)
        val workManager = WorkManager.getInstance(context)
        val constraints =
            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
        val workRequest = OneTimeWorkRequestBuilder<SendChatMessageWorker>()
            .setInputData(workDataOf(WORKER_MESSAGE_KEY to newMessageString))
            .setConstraints(constraints)
            .build()

        workManager.enqueue(workRequest)
    }
}