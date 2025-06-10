package com.iyke.ozix.domain.model.message

data class FromApiMessage(
    val id: String,
    val senderId: String,
    val chatId: String,
    val body: String,
    val timestamp: Long,
    val meta: String? = ""
)

data class ToApiMessage(
    val senderId: String,
    val chatId: String? = null,
    val recipientUserId: String? = null,
    val body: String,
)

fun FromApiMessage.toMessage(): Message {
    return Message(
        id = id,
        senderId = senderId,
        chatId = chatId,
        body = body,
        timestamp = timestamp,
        meta = "",
        sent = true
    )
}

fun Message.toApiMessage(recipientUserId: String? = null) = ToApiMessage(
    senderId = senderId,
    chatId = chatId,
    recipientUserId = recipientUserId,
    body = body
)