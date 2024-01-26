package com.othadd.ozi.ui.exploreScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.othadd.ozi.domain.model.OperationOutcome
import com.othadd.ozi.domain.model.OziData
import com.othadd.ozi.domain.model.User
import com.othadd.ozi.domain.repos.SearchHistoryRepo
import com.othadd.ozi.domain.useCases.interfaces.chat.CreatePairChatUseCase
import com.othadd.ozi.domain.useCases.interfaces.user.GetUsersUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val getUsersUseCases: GetUsersUseCases,
    private val createPairChatUseCase: CreatePairChatUseCase,
    private val searchHistoryRepo: SearchHistoryRepo,
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
            searchHistoryRepo.addToSearchHistory(search)
        }
    }

    fun removePrevSearch(search: String) {
        viewModelScope.launch {
            searchHistoryRepo.removeFromSearchHistory(search)
        }
    }

    fun clearPrevSearches() {
        viewModelScope.launch {
            searchHistoryRepo.clearSearchHistory()
        }
    }

    suspend fun createChat(user: User): String {
        return createPairChatUseCase(user)
    }

    init {
        viewModelScope.launch {
            searchHistoryRepo.getSearchHistory().collect {
                _uiState.value = _uiState.value.copy(prevSearches = it)
            }
        }
    }
}