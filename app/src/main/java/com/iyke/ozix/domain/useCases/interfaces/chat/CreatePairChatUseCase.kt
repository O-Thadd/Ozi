package com.iyke.ozix.domain.useCases.interfaces.chat

import com.iyke.ozix.domain.model.User

interface CreatePairChatUseCase {
    suspend operator fun invoke(chatMate: User): String
}