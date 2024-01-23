package com.othadd.ozi.domain.useCases.chat

import com.othadd.ozi.domain.model.User
import com.othadd.ozi.domain.useCases.interfaces.chat.CreatePairChatUseCase

class FakeCreatePairChatUseCase: CreatePairChatUseCase {
    override suspend fun invoke(chatMate: User): String {
        TODO("Not yet implemented")
    }
}