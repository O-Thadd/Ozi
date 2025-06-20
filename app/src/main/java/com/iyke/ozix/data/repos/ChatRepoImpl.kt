package com.iyke.ozix.data.repos

import com.iyke.ozix.data.dataSources.database.daos.ChatDao
import com.iyke.ozix.data.dataSources.localStore.OziDataStore
import com.iyke.ozix.data.dataSources.model.ApiCall
import com.iyke.ozix.data.dataSources.model.OpError
import com.iyke.ozix.data.dataSources.remote.OziRemoteService
import com.iyke.ozix.domain.model.OperationOutcomeX
import com.iyke.ozix.domain.model.chat.ApiChat
import com.iyke.ozix.domain.model.chat.Chat
import com.iyke.ozix.domain.model.chat.deriveUpdatedVersionFromDto
import com.iyke.ozix.domain.model.chat.toChat
import com.iyke.ozix.domain.model.chat.toChatDto
import com.iyke.ozix.domain.repos.ChatRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ChatRepoImpl @Inject constructor(
    private val chatDao: ChatDao,
    private val remoteService: OziRemoteService,
    private val dataStore: OziDataStore
) : ChatRepo {
    override suspend fun getChats(): Flow<List<Chat>> {
        return chatDao.getChats()
    }

    override suspend fun getChatsFromNetwork(): List<Chat> {
        remoteService.getUserChats()

        val outcome = ApiCall<List<ApiChat>, List<Chat>, OpError> {
            action { remoteService.getUserChats() }

            mapperOnSuccess { apiChats -> OperationOutcomeX.Successful(apiChats.map { it.toChat() }) }

            failure { OperationOutcomeX.Failed() }
        }

        return if (outcome is OperationOutcomeX.Successful) { outcome.data!! } else { emptyList() }
    }

    override suspend fun getChat(chatId: String): Chat? {
        return chatDao.getChatByChatId(chatId)
    }

    override suspend fun getChatFlow(chatId: String): Flow<Chat?> {
        return chatDao.getChatFlowByChatId(chatId)
    }

    override suspend fun addChat(chat: Chat) {
        chatDao.insert(chat)
    }

    override suspend fun updateChat(chat: Chat) {
        chatDao.update(chat)
    }

    override suspend fun syncChat(chatId: String, setUnread: Boolean) {
        withContext(Dispatchers.IO) {
            val apiChat = ApiChat(chatId)
            val dbChat = chatDao.getChatByChatId(chatId)
            if (dbChat != null) {
                val updatedChat = if (setUnread) {
                    dbChat.deriveUpdatedVersionFromDto(apiChat.toChatDto()).copy(hasUnreadMessage = true)
                } else {
                    dbChat.deriveUpdatedVersionFromDto(apiChat.toChatDto())
                }
                chatDao.update(updatedChat)
            } else {
                chatDao.insert(if (setUnread) apiChat.toChat(true) else apiChat.toChat())
            }
        }
    }

    override suspend fun deleteChats(chats: List<Chat>) {
        chatDao.delete(chats)
    }

    override suspend fun deleteAll() {
        chatDao.deleteAll()
    }
}