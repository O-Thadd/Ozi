package com.othadd.ozi.ui.chat

import com.othadd.ozi.domain.model.message.ChatItem

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
