package com.othadd.ozi.domain.model.chat

sealed class UiChat(
    val chatId: String,
    val chatName: String,
    val lastMessage: String,
    val lastMessageTimestamp: Long,
    val lastMessageDateTime: String,
    val aviFg: Int,
    val aviBg: Int,
    val hasUnreadMessage: Boolean
) {

    class PairChat(
        chatId: String,
        chatName: String,
        lastMessage: String,
        lastMessageTimestamp: Long,
        lastMessageDateTime: String,
        aviFg: Int,
        aviBg: Int,
        hasUnreadMessage: Boolean,
        val verified: Boolean
    ) : UiChat(
        chatId = chatId,
        chatName = chatName,
        lastMessage = lastMessage,
        lastMessageTimestamp = lastMessageTimestamp,
        lastMessageDateTime = lastMessageDateTime,
        aviFg = aviFg,
        aviBg = aviBg,
        hasUnreadMessage = hasUnreadMessage
    )

    class GroupChat(
        chatId: String,
        chatName: String,
        lastMessage: String,
        lastMessageTimestamp: Long,
        lastMessageDateTime: String,
        aviFg: Int,
        aviBg: Int,
        hasUnreadMessage: Boolean
    ): UiChat(
        chatId = chatId,
        chatName = chatName,
        lastMessage = lastMessage,
        lastMessageTimestamp = lastMessageTimestamp,
        lastMessageDateTime = lastMessageDateTime,
        aviFg = aviFg,
        aviBg = aviBg,
        hasUnreadMessage = hasUnreadMessage
    )
}