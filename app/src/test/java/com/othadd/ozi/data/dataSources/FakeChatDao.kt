package com.othadd.ozi.data.dataSources

import com.othadd.ozi.data.dataSources.database.daos.ChatDao
import com.othadd.ozi.domain.model.chat.Chat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf

class FakeChatDao: ChatDao {
    private val chats = HashMap<String, Chat>()
    private val chatsFlow = MutableStateFlow(chats.values.toList())

    override suspend fun insert(chat: Chat) {
        chats[chat.chatId] = chat
        chatsFlow.value = chats.values.toList()
    }

    override suspend fun delete(chats: List<Chat>) {
        for (chat in chats){
            this.chats.remove(chat.chatId)
        }
        chatsFlow.value = this.chats.values.toList()
    }

    override fun deleteAll() {
        chats.clear()
        chatsFlow.value = chats.values.toList()
    }

    override suspend fun update(chat: Chat) {
        chats[chat.chatId] = chat
        chatsFlow.value = chats.values.toList()
    }

    override fun getChats(): Flow<List<Chat>> {
        return chatsFlow
    }

    override fun getChatFlowByChatId(chatId: String): Flow<Chat?> {
        return flowOf(chats[chatId])
    }

    override fun getChatByChatId(chatId: String): Chat? {
        return chats[chatId]
    }
}