package com.iyke.ozix.ui.chatScreen

import com.iyke.ozix.domain.model.message.ChatItem

data class ChatUiState(
    val chatId: String,
    val chatName: String,
    val aviFg: Int,
    val aviBg: Int,
    val verified: Boolean,
    val chatItems: List<ChatItem>
) {
    companion object {
        val DEFAULT = ChatUiState("", "", -1, -1, false, emptyList())
    }
}