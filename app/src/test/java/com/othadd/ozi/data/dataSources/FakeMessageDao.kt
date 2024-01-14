package com.othadd.ozi.data.dataSources

import com.othadd.ozi.data.dataSources.database.daos.MessageDao
import com.othadd.ozi.domain.model.message.Message
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf

class FakeMessageDao: MessageDao {
    private val messages = mutableListOf<Message>()
    private val messagesFlow = MutableStateFlow(messages.toList())

    override suspend fun insert(message: Message) {
        messages.add(message)
        messagesFlow.value = messages
    }

    override suspend fun insertMultiple(messages: List<Message>) {
        this.messages.addAll(messages)
        messagesFlow.value = this.messages
    }

    override suspend fun delete(message: Message) {
        messages.remove(message)
        messagesFlow.value = messages
    }

    override suspend fun deleteMultiple(messages: List<Message>) {
        this.messages.removeAll(messages)
        messagesFlow.value = this.messages
    }

    override suspend fun deleteAll() {
        messages.clear()
        messagesFlow.value = messages
    }

    override suspend fun update(message: Message) {
        messages.find { it.id == message.id }?.let { messages.remove(it) }
        messages.add(message)
        messagesFlow.value = messages
    }

    override fun getAllMessages(): Flow<List<Message>> {
        return messagesFlow
    }

    override fun getMessagesByChatId(chatId: String): Flow<List<Message>> {
        return flowOf(messages.filter { it.chatId == chatId })
    }
}