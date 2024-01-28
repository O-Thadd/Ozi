package com.othadd.ozi.ui

import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import com.othadd.ozi.common.BOTTOM_BAR_EXPLORE_BUTTON
import com.othadd.ozi.common.BOTTOM_BAR_HOME_BUTTON
import com.othadd.ozi.common.OZI_APP_URI
import com.othadd.ozi.ui.chatScreen.CHAT_ID_KEY
import com.othadd.ozi.ui.chatScreen.ChatScreen
import com.othadd.ozi.ui.exploreScreen.ExploreScreen
import com.othadd.ozi.ui.groupGameSetupScreen.GroupGameSetupScreen
import com.othadd.ozi.ui.homeScreen.HomeScreen
import com.othadd.ozi.ui.model.Destination
import com.othadd.ozi.ui.onboarding.onBoardingGraph
import com.othadd.ozi.ui.profileScreen.ProfileScreen
import com.othadd.ozi.ui.theme.OziComposeTheme
import com.othadd.ozi.R

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun OziApp(
    mainActivityViewModel: MainActivityViewModel,
    updateCurrentDestination: (String) -> Unit,
    switchTheme: () -> Unit,
    sortOutNotificationPermission: () -> Unit,
    runOnUiThread: (() -> Unit) -> Unit,
    setUiReady: () -> Unit,
    exitApp: () -> Unit
) {
    val navController = rememberNavController()
    val appIuState by mainActivityViewModel.uiState.collectAsStateWithLifecycle(null)
    val showBottomBar = remember(appIuState) {
        appIuState?.currentDestination in listOf(
            Destination.HOME.route,
            Destination.EXPLORE.route
        )
    }
    val gameDialog by mainActivityViewModel.gamePrepDialogFlow.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            NavHost(
                navController = navController,
                startDestination = Destination.HOME.route,
                enterTransition = { slideInHorizontally { fullWidth -> fullWidth } },
                exitTransition = { slideOutHorizontally { fullWidth -> -fullWidth } },
                popEnterTransition = { slideInHorizontally { fullWidth -> -fullWidth } },
                popExitTransition = { slideOutHorizontally { fullWidth -> fullWidth } }
            ) {

                onBoardingGraph(
                    navController = navController,
                    updateCurrentDestination = updateCurrentDestination,
                    setUiReady = setUiReady,
                    exitApp = exitApp
                )

                composable(Destination.HOME.route) {
                    HomeScreen(
                        onChatClick = {
                            navController.navigate("${Destination.CHAT.route}/$it")
                        },
                        goToOnBoarding = { navController.navigate(Destination.ONBOARDING.route) },
                        updateCurrentDestination = updateCurrentDestination,
                        goToGroupGameSetup = { navController.navigate(Destination.GROUP_GAME_SETUP.route) },
                        logout = { mainActivityViewModel.confirmLogout() },
                        goToProfile = {
                            updateCurrentDestination(Destination.PROFILE.route)
                            navController.navigate(Destination.PROFILE.route)
                        },
                        goToDeveloper = {
                            updateCurrentDestination(Destination.DEVELOPER.route)
                            navController.navigate(Destination.DEVELOPER.route)
                        },
                        currentTheme = if (appIuState?.appState?.darkTheme == true) ThemeState.DARK else ThemeState.LIGHT,
                        switchTheme = switchTheme,
                        sortOutNotificationPermission = sortOutNotificationPermission,
                        setUiReady = setUiReady
                    )
                }

                composable(
                    route = "${Destination.CHAT.route}/{$CHAT_ID_KEY}",
                    deepLinks = listOf(navDeepLink { uriPattern = "$OZI_APP_URI/{$CHAT_ID_KEY}" })
                ) {
                    ChatScreen(
                        updateCurrentDestination = updateCurrentDestination,
                        confirmSendGameRequest = { ids, chatmateName ->
                            mainActivityViewModel.confirmSendGameRequest(ids, chatmateName)
                        },
                        gameModeratorId = appIuState?.appState?.gameModeratorId,
                        goBack = { navController.popBackStack() },
                        setUiReady = setUiReady
                    )
                }

                composable(Destination.EXPLORE.route) {
                    ExploreScreen(
                        onUserClicked = {
                            runOnUiThread {
                                navController.navigate("${Destination.CHAT.route}/$it") {
                                    popUpTo(Destination.HOME.route)
                                }
                            }
                        },
                        onBackClicked = { navController.popBackStack() },
                        updateCurrentDestination = updateCurrentDestination,
                        setUiReady = setUiReady
                    )
                }

                composable(Destination.GROUP_GAME_SETUP.route) {
                    GroupGameSetupScreen(
                        onBackClicked = { navController.popBackStack() },
                        updateCurrentDestination = updateCurrentDestination,
                        postDialog = { mainActivityViewModel.postDialog(it) },
                        postNotifyDialog = { mainActivityViewModel.postNotifyDialog(it) },
                        setUiReady = setUiReady
                    )
                }

                composable(
                    route = Destination.PROFILE.route
                ) {
                    ProfileScreen(
                        goBack = { navController.popBackStack() },
                        setUiReady = setUiReady
                    )
                }

                composable(Destination.DEVELOPER.route) {
                    DevScreen(
                        setUiReady = setUiReady
                    ) { navController.popBackStack() }
                }
            }
        }

        AnimatedVisibility(
            visible = appIuState?.dialog != null || gameDialog != null,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.align(Alignment.Center)
        ) {
            Surface(
                color = Color.Black.copy(alpha = 0.5f)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxSize()
                ) {
                    if (gameDialog != null) {
                        GamePrepDialog(
                            dialogData = gameDialog!!,
                            postAlertDialog = { title, body ->
                                mainActivityViewModel.postAlertDialog(title, body)
                            }
                        )
                    } else {
                        appIuState?.dialog?.let {
                            Dialog(dialogData = it)
                        }
                    }
                }

                BackHandler { }
            }
        }

        Box(
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            AnimatedVisibility(
                visible = showBottomBar,
                enter = slideInVertically { fullHeight -> fullHeight },
                exit = slideOutVertically { fullHeight -> fullHeight }
            ) {
                BottomBar(
                    currentDestination = appIuState?.currentDestination,
                    goToHome = {
                        navController.navigate(Destination.HOME.route) {
                            popUpTo(Destination.HOME.route) { inclusive = true }
                        }
                    },
                    goToExplore = { navController.navigate(Destination.EXPLORE.route) }
                )
            }
        }
    }

    LaunchedEffect(key1 = appIuState?.appState?.gameChatId) {
        val gameChatId = appIuState?.appState?.gameChatId
        if (gameChatId != null) {
            navController.navigate("${Destination.CHAT.route}/$gameChatId") {
                popUpTo(Destination.HOME.route) {
                    inclusive = false
                }
            }
        }
    }
}

@Composable
fun BottomBar(
    currentDestination: String?,
    goToHome: () -> Unit,
    goToExplore: () -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.background,
        tonalElevation = 3.dp
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.home_icon),
                contentDescription = null,
                tint = when (currentDestination) {
                    Destination.EXPLORE.route -> MaterialTheme.colorScheme.onBackground
                    else -> MaterialTheme.colorScheme.primary
                },
                modifier = Modifier
                    .semantics { contentDescription = BOTTOM_BAR_HOME_BUTTON }
                    .clickable {
                        if (currentDestination != Destination.HOME.route) {
                            goToHome()
                        }
                    }
                    .size(30.dp)
            )

            Icon(
                painter = painterResource(id = R.drawable.explore_icon),
                contentDescription = null,
                tint = when (currentDestination) {
                    Destination.EXPLORE.route -> MaterialTheme.colorScheme.primary
                    else -> MaterialTheme.colorScheme.onBackground
                },
                modifier = Modifier
                    .semantics { contentDescription = BOTTOM_BAR_EXPLORE_BUTTON }
                    .clickable {
                        if (currentDestination != Destination.EXPLORE.route) {
                            goToExplore()
                        }
                    }
                    .size(30.dp)
            )
        }
    }
}


//@Preview(widthDp = 412, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun PrevBottomBar() {
    OziComposeTheme {
        BottomBar(
            currentDestination = Destination.HOME.route,
            goToHome = { },
            goToExplore = { }
        )
    }
}
