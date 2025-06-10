package com.iyke.ozix.ui.groupGameSetupScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iyke.ozix.domain.gaming.GamingUseCases
import com.iyke.ozix.domain.model.OperationOutcome
import com.iyke.ozix.domain.model.OperationOutcomeX
import com.iyke.ozix.domain.model.OziData
import com.iyke.ozix.domain.model.User
import com.iyke.ozix.domain.repos.ChatRepo
import com.iyke.ozix.domain.repos.ThisUserRepo
import com.iyke.ozix.domain.useCases.interfaces.user.GetUsersUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameSetupViewModel @Inject constructor(
    private val gamingUseCases: GamingUseCases,
    private val thisUserRepo: ThisUserRepo,
    private val usersUseCase: GetUsersUseCases,
    private val chatRepo: ChatRepo,
): ViewModel() {

    private val _uiState = MutableStateFlow(GameSetupUiState.DEFAULT)
    val uiState = _uiState.asStateFlow()

    fun updateSelectedUsers(user: User, newSelectionStatus: Boolean): Boolean{
        if (newSelectionStatus){
            if (_uiState.value.selectedUsers.size == 5){
                return false
            }

            val newSelectedUsers = _uiState.value.selectedUsers.toMutableList().apply { add(user) }
            _uiState.value = _uiState.value.copy(selectedUsers = newSelectedUsers)
        }
        else{
            val newSelectedUsers = _uiState.value.selectedUsers.toMutableList().apply { remove(user) }
            _uiState.value = _uiState.value.copy(selectedUsers = newSelectedUsers)
        }

        return true
    }

    fun search(searchTerm: String) {
        viewModelScope.launch {
            if (searchTerm.isBlank()){
                _uiState.value = _uiState.value.copy(users = OziData.Available(getChatMates()))
                return@launch
            }

            _uiState.value = _uiState.value.copy(users = OziData.Fetching())
            val searchOutcome = usersUseCase.search(searchTerm)
            if (searchOutcome is OperationOutcomeX.Failed) {
                _uiState.value = _uiState.value.copy(users = OziData.Error())
            } else {
                _uiState.value =
                    _uiState.value.copy(users = OziData.Available((searchOutcome as OperationOutcomeX.Successful).data!!))
            }
        }
    }

    suspend fun sendRequest(): OperationOutcome<Nothing, Nothing> {
        return gamingUseCases.sendGameRequest(_uiState.value.selectedUsers.map { it.userId })
    }

    private suspend fun getChatMates(): List<User> {
        val thisUserId = thisUserRepo.get()!!.userId
        val localUsers =
            chatRepo.getChats().first().filter { it.chatId.length > 40 }.map { chat ->
                chat.participantIds.find { it != thisUserId }!!
                    .let { usersUseCase.getUser(it)!! }
            }
        return localUsers
    }

    init {
        viewModelScope.launch {
            val localUsers = getChatMates()
            _uiState.value = GameSetupUiState(emptyList(), OziData.Available(localUsers))
        }
    }
}