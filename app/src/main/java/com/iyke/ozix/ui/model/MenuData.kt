package com.iyke.ozix.ui.model

data class MenuData(
    val name: String,
    val action: () -> Unit,
    val iconResourceId: Int? = null,
)
