package com.iyke.ozix.ui.homeScreen

import com.iyke.ozix.domain.model.chat.UiChat
import com.iyke.ozix.domain.model.User

data class HomeUiState(
    val thisUser: User?,
    val chats: List<UiChat>,
) {
    companion object {
        val DEFAULT = HomeUiState(null, emptyList())
    }
}
