package com.iyke.ozix.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iyke.ozix.data.repos.LoginError
import com.iyke.ozix.data.repos.SignUpError
import com.iyke.ozix.domain.model.OperationOutcomeX
import com.iyke.ozix.domain.useCases.interfaces.user.LoginUserUseCase
import com.iyke.ozix.domain.useCases.interfaces.user.RegisterUserUseCase
import com.iyke.ozix.domain.useCases.interfaces.user.ThisUserUseCases
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
                if (opOutcome is OperationOutcomeX.Successful) {
                    _uiState.value = _uiState.value.copy(signedIn = true)
                    return@registerUserUseCase
                }
                opOutcome as OperationOutcomeX.Failed
                if (opOutcome.cause == SignUpError.USER_NAME_NOT_AVAILABLE) {
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
                if (opOutcome is OperationOutcomeX.Successful) {
                    _uiState.value = _uiState.value.copy(signedIn = true)
                    return@loginUserUseCase
                }
                opOutcome as OperationOutcomeX.Failed
                if (opOutcome.cause == LoginError) {
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