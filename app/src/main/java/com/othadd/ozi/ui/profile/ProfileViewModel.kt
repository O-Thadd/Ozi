package com.othadd.ozi.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.othadd.ozi.common.BUSY
import com.othadd.ozi.common.DEFAULT
import com.othadd.ozi.common.FAILED
import com.othadd.ozi.common.PASSED
import com.othadd.ozi.domain.model.OperationOutcome
import com.othadd.ozi.domain.model.OziData
import com.othadd.ozi.domain.model.User
import com.othadd.ozi.domain.useCases.interfaces.user.ThisUserUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val thisUserUseCases: ThisUserUseCases
): ViewModel() {

    private val userDataFlow = MutableStateFlow<OziData<User?>>(OziData.Available(null))
    private val updateStatusFlow = MutableStateFlow(DEFAULT)
    var uiState = combine(userDataFlow, updateStatusFlow){ userData, updateStatus ->
        ProfileUiState(userData, updateStatus)
    }.stateIn(viewModelScope, SharingStarted.Eagerly, ProfileUiState(OziData.Fetching(), DEFAULT))

    fun refresh(){
        viewModelScope.launch {
            userDataFlow.value = OziData.Fetching()
            val outcome = thisUserUseCases.refresh()
            if (outcome is OperationOutcome.Successful){
                userDataFlow.value = OziData.Available(thisUserUseCases.get().first())
                return@launch
            }
            userDataFlow.value = OziData.Error()
        }
    }

    fun update(username: String, aviFg: Int, aviBg: Int){
        viewModelScope.launch {
            updateStatusFlow.value = BUSY
            val user = thisUserUseCases.get().first()
            if (user != null) {
                val outcome = thisUserUseCases.updateRemote(user.copy(username = username, aviFg = aviFg, aviBg = aviBg))
                if (outcome is OperationOutcome.Successful){
                    refresh()
                    updateStatusFlow.value = PASSED
                }
                else{
                    updateStatusFlow.value = FAILED
                }
            }
            delay(200)
            updateStatusFlow.value = DEFAULT
        }
    }


    init {
        viewModelScope.launch {
            thisUserUseCases.get().collect{
                userDataFlow.value = OziData.Available(it)
            }
        }
    }
}