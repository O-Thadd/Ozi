package com.othadd.ozi.ui.onboarding.screens

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.othadd.ozi.ui.model.Destination
import com.othadd.ozi.ui.onboarding.OnBoardingViewModel
import com.othadd.ozi.ui.theme.OziComposeTheme
import com.othadd.oziX.R

@Composable
fun LandingScreen(
    updateCurrentDestination: (String) -> Unit,
    goToRegister: () -> Unit,
    goToLogin: () -> Unit,
    goToHome: () -> Unit,
    setUiReady: () -> Unit,
    exitApp: () -> Unit
) {
    val viewmodel: OnBoardingViewModel = hiltViewModel()
    val uiState by viewmodel.uiState.collectAsStateWithLifecycle()
    LandingScreen_simple(
        signedIn = uiState.signedIn,
        goToRegister = goToRegister,
        goToLogin = goToLogin,
        goToHome = goToHome
    )

    BackHandler {
        exitApp()
    }

    LaunchedEffect(key1 = true){
        updateCurrentDestination(Destination.ONBOARDING.route)
        setUiReady()
    }
}

@Composable
private fun LandingScreen_simple(
    signedIn: Boolean,
    goToRegister: () -> Unit,
    goToLogin: () -> Unit,
    goToHome: () -> Unit
) {

    var firstTime by rememberSaveable {
        mutableStateOf(true)
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.landing_page_art),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )

        val visible = remember {
            MutableTransitionState(!firstTime).apply { targetState = true }
        }

        AnimatedVisibility(
            visibleState = visible,
            enter = fadeIn(
                animationSpec = tween(delayMillis = 1000, durationMillis = 1000)
            )
        ) {
            Surface(
                color = MaterialTheme.colorScheme.primary.copy(0.9f),
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                ) {

                    Icon(
                        painter = painterResource(id = R.drawable.ic_app_icon),
                        contentDescription = null,
                        modifier = Modifier.size(250.dp)
                    )

                    Text(
                        text = "Ozi",
                        style = MaterialTheme.typography.headlineLarge.copy(fontSize = 40.sp)
                    )

                    Spacer(modifier = Modifier.height(120.dp))

                    FilledTonalButton(
                        onClick = goToRegister,
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(vertical = 16.dp),
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = MaterialTheme.colorScheme.onPrimary,
                            contentColor = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Register",
                            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedButton(
                        onClick = goToLogin,
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(2.dp, MaterialTheme.colorScheme.onPrimary),
                        contentPadding = PaddingValues(vertical = 16.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Login",
                            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp)
                        )
                    }

                    Spacer(modifier = Modifier.height(120.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Can you type faster than a Ninja?",
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Image(
                            painter = painterResource(R.drawable.ninja_emoji),
                            contentDescription = null,
                            modifier = Modifier.size(30.dp)
                        )
                    }

                }
            }
        }
    }

    LaunchedEffect(key1 = true){
        firstTime = false
    }

    if (signedIn) {
        LaunchedEffect(key1 = true){
            goToHome()
        }
    }
}

@Preview(showBackground = true, widthDp = 412, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PrevLandingScreen() {
    OziComposeTheme {
        LandingScreen_simple(
            goToRegister = { },
            goToLogin = { },
            goToHome = {  },
            signedIn = false
        )
    }
}