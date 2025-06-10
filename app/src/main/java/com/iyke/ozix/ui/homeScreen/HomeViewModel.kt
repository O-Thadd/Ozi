package com.iyke.ozix.ui.homeScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iyke.ozix.domain.useCases.defaultImplementations.ProcessSignalsUseCaseImpl
import com.iyke.ozix.domain.useCases.interfaces.chat.DeleteChatsUseCase
import com.iyke.ozix.domain.useCases.interfaces.chat.GetChatsUseCase
import com.iyke.ozix.domain.useCases.interfaces.user.ThisUserUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class HomeViewModel @Inject constructor(
    private val thisUserUseCases: ThisUserUseCases,
    private val deleteChatsUseCase: DeleteChatsUseCase,
    getChatsUseCase: GetChatsUseCase,
    processSignalsUseCaseImpl: ProcessSignalsUseCaseImpl,
) : ViewModel() {

    val uiState = MutableStateFlow<HomeUiState?>(null)

    open fun refreshThisUser() {
        viewModelScope.launch {
            try {
                thisUserUseCases.refresh()
            } catch (_: Exception) { }
        }
    }

    open fun clearEmptyChats(){
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

//        viewModelScope.launch {
//            var userLoggedIn = false
//            var loopCount = 0
//            coroutineScope {
//                launch {
//                    while (true) {
//                        loopCount++
//                        Log.e("zzz", "loop $loopCount running...")
//                        if (!userLoggedIn) {
//                            userLoggedIn = thisUserUseCases.get().first() != null
//                            Log.e("zzz", "user not logged in moving to next")
//                            delay(6000)
//                            continue
//                        }
//
//                        Log.e("zzz", "user logged in. refreshing...")
//                        processSignalsUseCaseImpl.refreshMessages()
//                        Log.e("zzz", "refresh done.")
//                        delay(6000)
//                    }
//                }
//            }
//        }
    }
}