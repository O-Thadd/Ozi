package com.othadd.ozi.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.othadd.ozi.domain.gaming.GamingUseCases
import com.othadd.ozi.domain.model.AppUiState
import com.othadd.ozi.domain.model.OperationOutcome
import com.othadd.ozi.domain.model.User
import com.othadd.ozi.domain.model.gaming.GamePrepOutcome
import com.othadd.ozi.domain.useCases.AppStateUseCases
import com.othadd.ozi.domain.useCases.NotificationUseCases
import com.othadd.ozi.domain.useCases.chat.LogoutUseCase
import com.othadd.ozi.domain.useCases.user.ThisUserUseCases
import com.othadd.ozi.ui.model.DialogData
import com.othadd.ozi.ui.model.GamePrepDialogData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val appStateUseCases: AppStateUseCases,
    private val notificationUseCases: NotificationUseCases,
    private val gamingUseCases: GamingUseCases,
    private val thisUserUseCases: ThisUserUseCases,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val dialogFlow = MutableStateFlow<DialogData?>(null)
    private val currentDestinationFlow = MutableStateFlow<String?>(null)
    val uiState =
        combine(
            appStateUseCases.getFlow(),
            dialogFlow,
            currentDestinationFlow
        ) { appState, dialog, currentDestination ->
            AppUiState(appState, dialog, currentDestination)
        }

    private val _toastFlow = MutableStateFlow<String?>(null)
    val toastFlow = _toastFlow.asStateFlow()

    private val _gamePrepDialogFlow = MutableStateFlow<GamePrepDialogData?>(null)
    val gamePrepDialogFlow = _gamePrepDialogFlow.asStateFlow()

    fun updateCurrentDestination(destinationRoute: String) {
        currentDestinationFlow.value = destinationRoute
    }

    fun incrementAppStartCount() {
        viewModelScope.launch {
            if (uiState.first().appState.signedIn) {
                appStateUseCases.incrementAppStartsCount()
            }
        }
    }

    fun clearNotifications() {
        viewModelScope.launch {
            notificationUseCases.clear()
        }
    }

    fun updateForegroundState(newValue: Boolean) {
        viewModelScope.launch {
            val appState = appStateUseCases.get()
            appStateUseCases.update(appState.copy(inForeground = newValue))
        }
    }

    fun confirmSendGameRequest(participantsIds: List<String>, chatMateName: String? = null) {
        val title = if (chatMateName != null) {
            "Send game request to $chatMateName?"
        } else {
            "Start group game?"
        }

        val button1 = Pair("Yes!") {
            sendGameRequest(participantsIds)
        }

        val button2 = Pair("Cancel") {
            postDialog(null)
        }

        val dialog = DialogData(title = title, button1 = button1, button2 = button2)
        dialogFlow.value = dialog
    }

    private fun sendGameRequest(participantsIds: List<String>) {
        viewModelScope.launch {
            val dialogTitle =
                if (participantsIds.size == 1) "Sending game request..." else "Starting group game..."
            dialogFlow.value = getNotifyDialog(dialogTitle)
            val outcome = gamingUseCases.sendGameRequest(participantsIds)
            if (outcome is OperationOutcome.Failed) {
                _toastFlow.value = "Failed to start game"
                dialogFlow.value = null
            }
        }
    }

    fun postDialog(dialog: DialogData?) {
        dialogFlow.value = dialog
    }

    fun postNotifyDialog(title: String){
        dialogFlow.value = getNotifyDialog(title)
    }

    fun postAlertDialog(title: String, body: String? = null){
        dialogFlow.value = getAlertDialog(title, body)
    }

    /**
     * Returns a dialog with only title. No buttons. Dialog should eventually be dismissed.
     */
    private fun getNotifyDialog(title: String): DialogData {
        return DialogData(title = title)
    }

    private fun getAlertDialog(title: String, body: String? = null): DialogData {
        val buttonAction = { dialogFlow.value = null }
        val button = Pair("Okay", buttonAction)
        return DialogData(title, body, button)
    }

    fun confirmLogout() {
        val button1Action = {
            dialogFlow.value = null
            logout()
        }
        val button1 = Pair("Yes", button1Action)
        val button2Action = { dialogFlow.value = null }
        val button2 = Pair("Cancel", button2Action)
        val dialog = DialogData(
            title = "Are you sure you want to logout?",
            button1 = button1,
            button2 = button2
        )
        dialogFlow.value = dialog
    }

    private fun logout() {
        viewModelScope.launch {
            logoutUseCase()
        }
    }

    fun changeAppTheme() {
        viewModelScope.launch {
            val currentTheme = appStateUseCases.get().darkTheme
            appStateUseCases.update(appStateUseCases.get().copy(darkTheme = !currentTheme))
        }
    }

    suspend fun getThisUser(): User? {
        return thisUserUseCases.get().first()
    }

    init {
        viewModelScope.launch {
            appStateUseCases.getFlow().collect {
                if (it.gamePrepState == null) {
                    _gamePrepDialogFlow.value = null
                    return@collect
                }

                // DISMISSING THE REGULAR DIALOG BEFORE SHOWING GAME DIALOG
                // this assumes that that game dialog is always related to any regular dialog that was showing.
                // so it makes sense from user perspective to dismiss the regular dialog before showing the game dialog.
                // if the assumption is not true, then this should be reimplemented
                dialogFlow.value = null
                _gamePrepDialogFlow.value = GamePrepDialogData(
                    state = it.gamePrepState,
                    accept = { viewModelScope.launch { gamingUseCases.acceptGame() } },
                    decline = { viewModelScope.launch { gamingUseCases.declineGame() } },
                    proceed = { viewModelScope.launch { gamingUseCases.proceed() } },
                    cancel = { viewModelScope.launch { gamingUseCases.cancelGame() } }
                )

                if (it.gamePrepState.prepOutcome == GamePrepOutcome.ZERO_ACCEPTANCE) {

                    val body =
                         "${if (it.gamePrepState.invitees.size == 1)"Invitee" else "Invitees"} not available or declined game request."
                    dialogFlow.value = getAlertDialog(
                        "Game Cancelled",
                        body
                    )
                }
            }
        }
    }
}