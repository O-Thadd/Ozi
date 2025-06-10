package com.iyke.ozix.domain.useCases.interfaces.message

interface MarkMessagesReadUseCase {
    suspend operator fun invoke(chatId: String)
}