package com.othadd.ozi.ui.profile

import com.othadd.ozi.domain.model.OziData
import com.othadd.ozi.domain.model.User

data class ProfileUiState(
    val userData: OziData<User?>,
    val updateStatus: Int
)
