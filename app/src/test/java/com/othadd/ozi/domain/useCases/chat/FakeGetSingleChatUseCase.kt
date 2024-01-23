package com.othadd.ozi.domain.useCases.chat

import com.othadd.ozi.domain.model.chat.Chat
import com.othadd.ozi.domain.useCases.interfaces.chat.GetSingleChatUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeGetSingleChatUseCase: GetSingleChatUseCase {
    override suspend fun invoke(chatId: String): Flow<Chat> {
        val chat = Chat("chatId", listOf("participant1id", "participant2id"), false)
        return flowOf(chat)
    }
}