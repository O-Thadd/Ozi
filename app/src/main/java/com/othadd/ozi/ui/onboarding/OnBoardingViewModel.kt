package com.othadd.ozi.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.othadd.ozi.domain.model.ServerResponseStatus
import com.othadd.ozi.domain.model.OperationOutcome
import com.othadd.ozi.domain.useCases.interfaces.user.LoginUserUseCase
import com.othadd.ozi.domain.useCases.interfaces.user.RegisterUserUseCase
import com.othadd.ozi.domain.useCases.interfaces.user.ThisUserUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val loginUserUseCase: LoginUserUseCase,
    private val registerUserUseCase: RegisterUserUseCase,
    thisUserUseCases: ThisUserUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        OnBoardingUiState(
            false,
            OnBoardingOpStatus.DEFAULT,
            OnBoardingOpStatus.DEFAULT
        )
    )
    val uiState = _uiState.asStateFlow()

    fun register(username: String, password: String, fg: Int, bg: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(registrationOpStatus = OnBoardingOpStatus.BUSY)

            registerUserUseCase(username, password, fg, bg, viewModelScope) { opOutcome ->
                if (opOutcome is OperationOutcome.Successful) {
                    _uiState.value = _uiState.value.copy(signedIn = true)
                    return@registerUserUseCase
                }
                opOutcome as OperationOutcome.Failed
                if (opOutcome.cause == ServerResponseStatus.FAILURE) {
                    _uiState.value =
                        _uiState.value.copy(registrationOpStatus = OnBoardingOpStatus.FAILED)
                } else {
                    _uiState.value =
                        _uiState.value.copy(registrationOpStatus = OnBoardingOpStatus.ERROR)
                }
            }
        }
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(loginOpStatus = OnBoardingOpStatus.BUSY)

            loginUserUseCase(username, password, viewModelScope) { opOutcome ->
                if (opOutcome is OperationOutcome.Successful) {
                    _uiState.value = _uiState.value.copy(signedIn = true)
                    return@loginUserUseCase
                }
                opOutcome as OperationOutcome.Failed
                if (opOutcome.cause == ServerResponseStatus.FAILURE) {
                    _uiState.value =
                        _uiState.value.copy(loginOpStatus = OnBoardingOpStatus.FAILED)
                } else {
                    _uiState.value =
                        _uiState.value.copy(loginOpStatus = OnBoardingOpStatus.ERROR)
                }
            }
        }
    }


    init {
        viewModelScope.launch {
            val signedIn = thisUserUseCases.get().first() != null
            _uiState.value = OnBoardingUiState(
                signedIn,
                OnBoardingOpStatus.DEFAULT,
                OnBoardingOpStatus.DEFAULT
            )

            thisUserUseCases.get().collect {
                _uiState.value = _uiState.value.copy(signedIn = it != null)
            }
        }
    }
}