package com.othadd.ozi.ui.model

import com.othadd.ozi.domain.model.gaming.GamePrepState

data class DialogData(
    val title: String? = null,
    val body: String? = null,
    val button1: Pair<String, () -> Unit>? = null,
    val button2: Pair<String, () -> Unit>? = null,
)

data class GamePrepDialogData(
    val state: GamePrepState,
    val accept: () -> Unit,
    val decline: () -> Unit,
    val proceed: () -> Unit,
    val cancel: () -> Unit
)
