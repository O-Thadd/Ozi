package com.othadd.ozi.domain.useCases.chat

import com.othadd.ozi.domain.model.chat.UiChat
import com.othadd.ozi.domain.useCases.interfaces.chat.GetChatsUseCase
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