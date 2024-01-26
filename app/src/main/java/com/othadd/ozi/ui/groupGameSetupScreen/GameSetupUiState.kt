package com.othadd.ozi.ui.groupGameSetupScreen

import com.othadd.ozi.domain.model.OziData
import com.othadd.ozi.domain.model.User

data class GameSetupUiState(
    val selectedUsers: List<User>,
    val users: OziData<List<User>>,
) {
    companion object {
        val DEFAULT = GameSetupUiState(emptyList(), OziData.Available(emptyList()))
    }
}
