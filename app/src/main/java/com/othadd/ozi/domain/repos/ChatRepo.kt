package com.othadd.ozi.domain.repos

import com.othadd.ozi.domain.model.chat.Chat
import kotlinx.coroutines.flow.Flow

interface ChatRepo {

    suspend fun getChats(): Flow<List<Chat>>

    suspend fun getChat(chatId: String): Chat?

    suspend fun getChatFlow(chatId: String): Flow<Chat?>

    /**
     * Performs an update instead if chat with same [Chat.chatId] already exists
     */
    suspend fun addChat(chat: Chat)

    suspend fun updateChat(chat: Chat)

    /**
     * Updates local chat with remote data.
     * Creates a new chat locally if no preexisting chat with provided [chatId] is found
     */
    suspend fun syncChat(chatId: String, setUnread: Boolean = false)

    suspend fun deleteChats(chats: List<Chat>)

    suspend fun deleteAll()
}