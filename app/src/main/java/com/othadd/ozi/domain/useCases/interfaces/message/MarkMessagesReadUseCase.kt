package com.othadd.ozi.domain.useCases.interfaces.message

interface MarkMessagesReadUseCase {
    suspend operator fun invoke(chatId: String)
}