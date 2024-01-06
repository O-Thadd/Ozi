package com.othadd.ozi.ui.onboarding

import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.othadd.ozi.ui.onboarding.screens.LandingScreen
import com.othadd.ozi.ui.onboarding.screens.LoginScreen
import com.othadd.ozi.ui.onboarding.screens.RegisterScreen
import com.othadd.ozi.ui.model.Destination
import com.othadd.ozi.ui.model.Destination.ONBOARDING

fun NavGraphBuilder.onBoardingGraph(
    navController: NavController,
    updateCurrentDestination: (String) -> Unit,
    exitApp: () -> Unit
){
    fun goToHome() {
        val navOptions = NavOptions.Builder().setPopUpTo(ONBOARDING.route, true).build()
        navController.navigate(Destination.HOME.route, navOptions)
        navController.popBackStack(Destination.HOME.route, true)
    }

    navigation(startDestination = ONBOARDING.LANDING.route, route = ONBOARDING.route){

        composable(ONBOARDING.LANDING.route) {
            LandingScreen(
                updateCurrentDestination = updateCurrentDestination,
                goToRegister = { navController.navigate(ONBOARDING.REGISTER.route) },
                goToLogin = { navController.navigate(ONBOARDING.LOGIN.route) },
                goToHome = { goToHome() },
                exitApp = exitApp
            )
        }

        composable(ONBOARDING.REGISTER.route) {
            RegisterScreen(
                context = LocalContext.current,
                goToHome = { goToHome() }
            )
        }

        composable(ONBOARDING.LOGIN.route) {
            LoginScreen(
                context = LocalContext.current,
                goToHome = { goToHome() }
            )
        }
    }
}