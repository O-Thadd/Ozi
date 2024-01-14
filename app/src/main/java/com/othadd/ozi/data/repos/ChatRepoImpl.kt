package com.othadd.ozi.data.repos

import com.othadd.ozi.data.dataSources.database.daos.ChatDao
import com.othadd.ozi.data.dataSources.localStore.DefaultOziDataStore
import com.othadd.ozi.data.dataSources.localStore.OziDataStore
import com.othadd.ozi.data.dataSources.remote.OziRemoteService
import com.othadd.ozi.domain.model.chat.Chat
import com.othadd.ozi.domain.model.chat.deriveUpdatedVersionFromDto
import com.othadd.ozi.domain.model.chat.toChat
import com.othadd.ozi.domain.repos.ChatRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class ChatRepoImpl @Inject constructor(
    private val chatDao: ChatDao,
    private val remoteService: OziRemoteService,
    private val dataStore: OziDataStore
) : ChatRepo {
    override suspend fun getChats(): Flow<List<Chat>> {
        return chatDao.getChats()
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
        val token = dataStore.getThisUserFlow().first()!!.token!!
        val chatDto = remoteService.getChats(chatId, token)[0]
        val dbChat = chatDao.getChatByChatId(chatId)
        if (dbChat != null) {
            val updatedChat = if (setUnread) {
                dbChat.deriveUpdatedVersionFromDto(chatDto).copy(hasUnreadMessage = true)
            } else {
                dbChat.deriveUpdatedVersionFromDto(chatDto)
            }
            chatDao.update(updatedChat)
        } else {
            chatDao.insert(if (setUnread) chatDto.toChat(true) else chatDto.toChat())
        }
    }

    override suspend fun deleteChats(chats: List<Chat>) {
        chatDao.delete(chats)
    }

    override suspend fun deleteAll() {
        chatDao.deleteAll()
    }
}