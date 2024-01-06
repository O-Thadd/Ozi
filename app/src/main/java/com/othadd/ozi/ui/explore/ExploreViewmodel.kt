package com.othadd.ozi.ui.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.othadd.ozi.domain.model.OperationOutcome
import com.othadd.ozi.domain.model.OziData
import com.othadd.ozi.domain.model.User
import com.othadd.ozi.domain.useCases.SearchHistoryUseCases
import com.othadd.ozi.domain.useCases.chat.CreatePairChatUseCase
import com.othadd.ozi.domain.useCases.user.GetUsersUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExploreViewmodel @Inject constructor(
    private val getUsersUseCases: GetUsersUseCases,
    private val searchHistoryUseCases: SearchHistoryUseCases,
    private val createPairChatUseCase: CreatePairChatUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExploreUiState.DEFAULT)
    val uiState = _uiState.asStateFlow()

    fun getExploreUsers() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(exploredUsers = OziData.Fetching())

            val outcome = getUsersUseCases.explore()
            val exploredUsers: OziData<List<User>> =
                if (outcome is OperationOutcome.Successful)
                    OziData.Available(outcome.data!!)
                else
                    OziData.Error()

            _uiState.value = _uiState.value.copy(exploredUsers = exploredUsers)
        }
    }

    fun searchUsers(searchTerm: String) {
        viewModelScope.launch {
            if (searchTerm.length < 3){
                return@launch
            }

            _uiState.value = _uiState.value.copy(searchedUsers = OziData.Fetching())

            val outcome = getUsersUseCases.search(searchTerm)
            val searchedUsers: OziData<List<User>> =
                if (outcome is OperationOutcome.Successful)
                    OziData.Available(outcome.data!!)
                else
                    OziData.Error()

            _uiState.value = _uiState.value.copy(searchedUsers = searchedUsers)
        }
    }

    fun addToPrevSearches(search: String) {
        viewModelScope.launch {
            searchHistoryUseCases.addToPrevSearch(search)
        }
    }

    fun removePrevSearch(search: String) {
        viewModelScope.launch {
            searchHistoryUseCases.removePrevSearch(search)
        }
    }

    fun clearPrevSearches() {
        viewModelScope.launch {
            searchHistoryUseCases.clearHistory()
        }
    }

    suspend fun createChat(user: User): String {
        return createPairChatUseCase(user)
    }

    init {
        viewModelScope.launch {
            searchHistoryUseCases.getPrevSearches().collect {
                _uiState.value = _uiState.value.copy(prevSearches = it)
            }
        }
    }
}