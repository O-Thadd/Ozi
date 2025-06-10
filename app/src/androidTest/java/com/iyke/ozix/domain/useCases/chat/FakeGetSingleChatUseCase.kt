package com.iyke.ozix.domain.useCases.chat

import com.iyke.ozix.domain.model.chat.Chat
import com.iyke.ozix.domain.useCases.interfaces.chat.GetSingleChatUseCase
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