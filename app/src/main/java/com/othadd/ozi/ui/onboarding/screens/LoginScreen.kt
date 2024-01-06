package com.othadd.ozi.ui.onboarding.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.othadd.ozi.ui.OnBoardingTextField
import com.othadd.ozi.ui.onboarding.OnBoardingOpStatus
import com.othadd.ozi.ui.onboarding.OnBoardingViewModel
import com.othadd.ozi.ui.theme.OziComposeTheme
import com.othadd.oziX.R

@Composable
fun LoginScreen(
    goToHome: () -> Unit,
    context: Context
) {
    val viewmodel: OnBoardingViewModel = hiltViewModel()
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val uiState by viewmodel.uiState.collectAsStateWithLifecycle()

    Box{
        Image(
            painter = painterResource(id = R.drawable.landing_page_art),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )

        Surface(
            color = MaterialTheme.colorScheme.background.copy(0.9f)
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.onboarding_corner_element),
                    tint = MaterialTheme.colorScheme.primary,
                    contentDescription = null,
                    modifier = Modifier
                        .size(250.dp)
                        .align(Alignment.TopEnd)
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 32.dp)
                ) {

                    Spacer(modifier = Modifier.height(32.dp))

                    OnBoardingTextField(
                        text = username,
                        onTextChanged = { username = it },
                        placeHolder = "Username"
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OnBoardingTextField(
                        text = password,
                        onTextChanged = { password = it },
                        placeHolder = "Password",
                        forPassword = true
                    )

                    Spacer(modifier = Modifier.height(64.dp))

                    FilledTonalButton(
                        onClick = { viewmodel.login(username, password) },
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(vertical = 16.dp),
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        enabled = uiState.loginOpStatus != OnBoardingOpStatus.BUSY
                                && username.isNotBlank()
                                && password.isNotBlank(),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        AnimatedContent(
                            targetState = uiState.loginOpStatus,
                            label = "button animation"
                        ) {
                            if (it != OnBoardingOpStatus.BUSY) {
                                Text(
                                    text = "Login",
                                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp)
                                )
                            } else {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(64.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Challenge your friends to a keyboard duel",
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Image(
                            painter = painterResource(R.drawable.duel_emoji),
                            contentDescription = null,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }
            }
        }
    }

    LaunchedEffect(key1 = true){
        viewmodel.uiState.collect {
            val toastMessage = when(it.loginOpStatus){
                OnBoardingOpStatus.ERROR -> "Could not login. Check network"
                OnBoardingOpStatus.FAILED -> "incorrect username or password"
                else -> null
            }
            toastMessage?.let {
                Toast.makeText(context, toastMessage, Toast.LENGTH_LONG).show()
            }

            if (it.signedIn){
                goToHome()
            }
        }
    }
}

@Preview
@Composable
fun PrevLoginScreen() {
    OziComposeTheme {
//        LoginScreen()
    }
}