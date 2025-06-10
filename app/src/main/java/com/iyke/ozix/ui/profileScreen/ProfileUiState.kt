package com.iyke.ozix.ui.profileScreen

import com.iyke.ozix.domain.model.OziData
import com.iyke.ozix.domain.model.User

data class ProfileUiState(
    val userData: OziData<User?>,
    val updateStatus: Int
)
