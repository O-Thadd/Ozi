package com.iyke.ozix.ui.groupGameSetupScreen

import com.iyke.ozix.domain.model.OziData
import com.iyke.ozix.domain.model.User

data class GameSetupUiState(
    val selectedUsers: List<User>,
    val users: OziData<List<User>>,
) {
    companion object {
        val DEFAULT = GameSetupUiState(emptyList(), OziData.Available(emptyList()))
    }
}
