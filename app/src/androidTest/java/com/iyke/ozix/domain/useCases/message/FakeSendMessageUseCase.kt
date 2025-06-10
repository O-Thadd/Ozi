package com.iyke.ozix.domain.useCases.message

import com.iyke.ozix.domain.model.message.Message
import com.iyke.ozix.domain.useCases.interfaces.message.SendMessageUseCase

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