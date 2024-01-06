package com.othadd.ozi.ui.onboarding

data class OnBoardingUiState(
    val signedIn: Boolean,
    val registrationOpStatus: OnBoardingOpStatus,
    val loginOpStatus: OnBoardingOpStatus,
)

enum class OnBoardingOpStatus{
    DEFAULT,
    BUSY,
    FAILED,
    SUCCESS,
    ERROR
}
