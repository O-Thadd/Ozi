package com.iyke.ozix.domain.useCases.chat

import com.iyke.ozix.domain.model.User
import com.iyke.ozix.domain.useCases.interfaces.chat.CreatePairChatUseCase

class FakeCreatePairChatUseCase: CreatePairChatUseCase {
    override suspend fun invoke(chatMate: User): String {
        TODO("Not yet implemented")
    }
}