package com.othadd.ozi.domain.useCases.chat

import com.othadd.ozi.domain.model.chat.Chat
import com.othadd.ozi.domain.useCases.interfaces.chat.GetSingleChatUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeGetSingleChatUseCase(private var chatToReturn: Chat): GetSingleChatUseCase {
    override suspend fun invoke(chatId: String): Flow<Chat> {
        return flowOf(chatToReturn)
    }

    fun setChatToReturn(chat: Chat){
        chatToReturn = chat
    }
}