package com.iyke.ozix.domain.model.chat

class ChatDto(
    val chatId: String,
    val participantsIds: List<String>
)

fun ChatDto.toChat(hasUnreadMessage: Boolean = false): Chat {
    return Chat(chatId, participantsIds, hasUnreadMessage)
}

data class ApiChat(
    val id: String,
)

fun ApiChat.toChat(hasUnreadMessage: Boolean = false) = Chat(
    chatId = id,
    participantIds = emptyList(),
    hasUnreadMessage = hasUnreadMessage
)

fun ApiChat.toChatDto() = ChatDto(
    chatId = id,
    participantsIds = emptyList()
)
