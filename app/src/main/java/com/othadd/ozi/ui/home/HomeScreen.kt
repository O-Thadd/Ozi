package com.othadd.ozi.ui.home

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.othadd.ozi.domain.model.User
import com.othadd.ozi.domain.model.chat.UiChat
import com.othadd.ozi.ui.Menu
import com.othadd.ozi.ui.ThemeState
import com.othadd.ozi.ui.model.Destination
import com.othadd.ozi.ui.model.MenuData
import com.othadd.ozi.ui.theme.OziComposeTheme
import com.othadd.ozi.testPairChats1
import com.othadd.ozi.testUser1
import com.othadd.oziX.R

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun HomeScreen(
    goToOnBoarding: () -> Unit,
    goToProfile: () -> Unit,
    goToDeveloper: () -> Unit,
    sortOutNotificationPermission: () -> Unit,
    onChatClick: (String) -> Unit,
    updateCurrentDestination: (String) -> Unit,
    goToGroupGameSetup: () -> Unit,
    logout: () -> Unit,
    currentTheme: ThemeState,
    switchTheme: () -> Unit
) {
    val viewmodel: HomeViewmodel = hiltViewModel()
    val uiState by viewmodel.uiState.collectAsStateWithLifecycle()

    if(uiState.thisUser != null) {
        HomeScreen_simple(
            thisUser = uiState.thisUser!!,
            chats = uiState.chats,
            startChat = onChatClick,
            goToGroupGameSetup = goToGroupGameSetup,
            logout = logout,
            goToProfile = goToProfile,
            goToDeveloper = goToDeveloper,
            currentTheme = currentTheme,
            switchTheme = switchTheme,
            sortOutNotificationPermission = sortOutNotificationPermission,
        )
    } else {
        LaunchedEffect(key1 = true) {
            goToOnBoarding()
        }
    }

    LaunchedEffect(key1 = uiState) {
        updateCurrentDestination(Destination.HOME.route)
    }

    LaunchedEffect(key1 = true) {
        viewmodel.clearEmptyChats()
        viewmodel.refreshThisUser()
    }
}

@Composable
private fun HomeScreen_simple(
    thisUser: User,
    chats: List<UiChat>,
    startChat: (String) -> Unit,
    goToGroupGameSetup: () -> Unit,
    logout: () -> Unit,
    goToProfile: () -> Unit,
    goToDeveloper: () -> Unit,
    currentTheme: ThemeState,
    switchTheme: () -> Unit,
    sortOutNotificationPermission: () -> Unit
) {

    var showMenu by remember { mutableStateOf(false) }

    val menuItems = remember {
        val groupGameMenuItem = MenuData(
            name = "New Group Game",
            action = { goToGroupGameSetup() },
            iconResourceId = R.drawable.ic_group
        )
        val developerMenuItem = MenuData(
            name = "Developer",
            action = { goToDeveloper() },
            iconResourceId = R.drawable.builder
        )
        val logOutMenuItem = MenuData(
            name = "Log out",
            action = { logout() },
            iconResourceId = R.drawable.ic_logout
        )

        listOf(groupGameMenuItem, developerMenuItem, logOutMenuItem)
    }

    Surface(
        color = MaterialTheme.colorScheme.background
    ) {
        Box {
            Column(
                Modifier.fillMaxHeight()
            ) {
                Spacer(modifier = Modifier.height(20.dp))

                HomeScreenTopBar(
                    thisUser = thisUser,
                    onAviPressed = goToProfile,
                    onMenuButtonPressed = { showMenu = !showMenu },
                    currentTheme = currentTheme,
                    switchTheme = switchTheme,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                Divider(thickness = Dp.Hairline)

                Box(
                    modifier = Modifier.weight(1f)
                ) {
                    Surface(
                        color = MaterialTheme.colorScheme.surface
                    ) {
                        if (chats.isNotEmpty()) {
                            Column(
                                Modifier
                                    .fillMaxHeight()
                                    .padding(horizontal = 16.dp)
                            ) {

                                Spacer(modifier = Modifier.height(20.dp))

                                Chats(
                                    chats = chats,
                                    onChatClicked = { startChat(it) }
                                )
                            }
                        } else {
                            Box(
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.ozi_empty_state),
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(400.dp)
                                        .align(Alignment.Center)
                                )

                                Text(
                                    text = "No chats yet. Your chats will appear here",
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier
                                        .align(Alignment.BottomCenter)
                                        .padding(bottom = 80.dp)
                                )
                            }
                        }
                    }
                }
            }

            AnimatedVisibility(
                visible = showMenu,
                enter = slideInVertically(),
                exit = slideOutVertically()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable(
                            onClick = { showMenu = false },
                            interactionSource = MutableInteractionSource(),
                            indication = null
                        )
                ) {
                    Menu(
                        menuItems = menuItems,
                        onAnyItemClicked = { showMenu = false },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(top = 80.dp, end = 16.dp)
                    )
                }
            }
        }
    }

    LaunchedEffect(key1 = true){
        sortOutNotificationPermission()
    }
}

//@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Preview(showBackground = true, widthDp = 412, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun PrevHome() {
    OziComposeTheme {
        HomeScreen_simple(
            thisUser = testUser1,
            chats = testPairChats1,
//            chats = emptyList(),
            startChat = { },
            goToGroupGameSetup = {  },
            logout = {  },
            goToProfile = {  },
            goToDeveloper = {  },
            currentTheme = ThemeState.LIGHT,
            switchTheme = {  },
            sortOutNotificationPermission = {  }
        )
    }
}



