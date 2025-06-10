package com.iyke.ozix.domain.useCases.chat

import com.iyke.ozix.domain.model.chat.UiChat
import com.iyke.ozix.domain.useCases.interfaces.chat.GetChatsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeGetChatsUseCase(private var chatsToReturn: List<UiChat>) : GetChatsUseCase {

    override suspend fun invoke(): Flow<List<UiChat>> {
        return flowOf(chatsToReturn)
    }

    fun updateChatsToReturn(chats: List<UiChat>) {
        chatsToReturn = chats
    }
}