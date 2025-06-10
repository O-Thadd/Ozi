package com.iyke.ozix.domain.useCases.interfaces.message

import com.iyke.ozix.domain.model.message.Message

interface SendMessageUseCase {
    suspend operator fun invoke(messageBody: String, chatId: String): Message
}