package com.othadd.ozi.domain.useCases.interfaces.chat

import com.othadd.ozi.domain.model.User

interface CreatePairChatUseCase {
    suspend operator fun invoke(chatMate: User): String
}