package com.othadd.ozi.domain.useCases.message

import com.othadd.ozi.domain.model.message.Message
import com.othadd.ozi.domain.useCases.interfaces.message.SendMessageUseCase

class FakeSendMessageUseCase: SendMessageUseCase {
    override suspend fun invoke(messageBody: String, chatId: String): Message {
        return Message(
            senderId = "senderId",
            chatId = chatId,
            body = messageBody,
            timestamp = 1L
        )
    }
}