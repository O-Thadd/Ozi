package com.iyke.ozix.domain.model

import com.iyke.ozix.domain.model.gaming.GamePrepState
import com.iyke.ozix.ui.model.DialogData

data class AppState(
    val darkTheme: Boolean,
    val gameModeratorId: String?,
    val gameBrokerId: String?,
    val appStartsCount: Int,
    val signedIn: Boolean,
    val inForeground: Boolean,
    val gamePrepState: GamePrepState?,
    val gameChatId: String?

) {
    companion object{
      val DEFAULT = AppState(
          darkTheme = false,
          gameModeratorId = null,
          gameBrokerId = null,
          appStartsCount = 0,
          signedIn = false,
          inForeground = false,
          gamePrepState = null,
          gameChatId = null
      )
    }
}

data class AppUiState(
    val appState: AppState,
    val dialog: DialogData?,
    val currentDestination: String?
)
