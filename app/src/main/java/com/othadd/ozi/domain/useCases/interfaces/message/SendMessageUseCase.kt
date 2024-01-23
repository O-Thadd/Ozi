package com.othadd.ozi.domain.useCases.interfaces.message

import com.othadd.ozi.domain.model.message.Message

interface SendMessageUseCase {
    suspend operator fun invoke(messageBody: String, chatId: String): Message
}