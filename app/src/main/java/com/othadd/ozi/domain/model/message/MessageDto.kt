package com.othadd.ozi.domain.model.message

data class MessageDto(
    val id: String,
    val senderId: String,
    val chatId: String,
    val body: String,
    val timestamp: Long,
    val meta: String
)

fun MessageDto.toMessage(): Message {
    return Message(
        id = id,
        senderId = senderId,
        chatId = chatId,
        body = body,
        timestamp = timestamp,
        meta = meta,
        sent = true
    )
}