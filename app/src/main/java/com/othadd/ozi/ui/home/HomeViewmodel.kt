package com.othadd.ozi.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.othadd.ozi.domain.useCases.chat.DeleteChatsUseCase
import com.othadd.ozi.domain.useCases.chat.GetChatsUseCase
import com.othadd.ozi.domain.useCases.user.ThisUserUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewmodel @Inject constructor(
    private val thisUserUseCases: ThisUserUseCases,
    private val deleteChatsUseCase: DeleteChatsUseCase,
    getChatsUseCase: GetChatsUseCase
) : ViewModel() {

    var uiState = MutableStateFlow(HomeUiState.DEFAULT)

    fun refreshThisUser() {
        viewModelScope.launch {
            try {
                thisUserUseCases.refresh()
            } catch (_: Exception) { }
        }
    }

    fun clearEmptyChats(){
        viewModelScope.launch {
            deleteChatsUseCase.deleteEmptyChats()
        }
    }

    init {
        viewModelScope.launch {
            combine(
                thisUserUseCases.get(),
                getChatsUseCase()
            ) { thisUser, chats ->
                HomeUiState(thisUser, chats)
            }.collect {
                uiState.value = it
            }
        }
    }
}