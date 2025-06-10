package com.iyke.ozix.ui.model

import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.iyke.ozix.ui.MainActivity
import javax.inject.Inject

interface SplashScreenSetupRunner {
    operator fun invoke(activity: MainActivity)
}

class DefaultSplashScreenSetupRunner @Inject constructor() : SplashScreenSetupRunner {
    override fun invoke(activity: MainActivity) {
        activity.installSplashScreen().setKeepOnScreenCondition {
            !activity.mainActivityViewModel.uiReady
        }
    }
}