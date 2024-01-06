package com.othadd.ozi.ui.home

import com.othadd.ozi.domain.model.chat.UiChat
import com.othadd.ozi.domain.model.User

data class HomeUiState(
    val thisUser: User?,
    val chats: List<UiChat>,
) {
    companion object {
        val DEFAULT = HomeUiState(null, emptyList())
    }
}
