package com.othadd.ozi.domain.model.chat

class ChatDto(
    val chatId: String,
    val participantsIds: List<String>
)

fun ChatDto.toChat(hasUnreadMessage: Boolean = false): Chat {
    return Chat(chatId, participantsIds, hasUnreadMessage)
}
