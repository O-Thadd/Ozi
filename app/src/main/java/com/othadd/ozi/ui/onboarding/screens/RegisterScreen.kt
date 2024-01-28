package com.othadd.ozi.ui.onboarding.screens

import android.content.Context
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.othadd.ozi.common.AVI_SELECTION_DIALOG_BUTTON
import com.othadd.ozi.ui.Avi
import com.othadd.ozi.ui.AviChooserDialog
import com.othadd.ozi.ui.OnBoardingTextField
import com.othadd.ozi.ui.onboarding.OnBoardingOpStatus
import com.othadd.ozi.ui.onboarding.OnBoardingViewModel
import com.othadd.ozi.ui.theme.OziComposeTheme
import com.othadd.ozi.R

@Composable
fun RegisterScreen(
    goToHome: () -> Unit,
    context: Context
) {
    val viewmodel: OnBoardingViewModel = hiltViewModel()
    var username by remember { mutableStateOf("") }
    val uiState by viewmodel.uiState.collectAsStateWithLifecycle()

    RegisterScreen_simple(
        username = username,
        onUsernameChanged = { username = it },
        registrationStatus = uiState.registrationOpStatus,
        register = { finalUsername, password, fg, bg ->
            viewmodel.register(finalUsername, password, fg, bg)
        }
    )

    LaunchedEffect(key1 = true) {
        viewmodel.uiState.collect {
            val toastMessage = when (it.registrationOpStatus) {
                OnBoardingOpStatus.ERROR -> "Could not register. Check network"
                OnBoardingOpStatus.FAILED -> "$username has been taken. Try a different username"
                else -> null
            }
            toastMessage?.let {
                Toast.makeText(context, toastMessage, Toast.LENGTH_LONG).show()
            }

            if (it.signedIn) {
                goToHome()
            }
        }
    }
}

@Composable
fun RegisterScreen_simple(
    username: String,
    onUsernameChanged: (String) -> Unit,
    registrationStatus: OnBoardingOpStatus,
    register: (String, String, Int, Int) -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground
    ) {
        var aviChooserShowing by remember { mutableStateOf(false) }

        var aviSelection by remember { mutableStateOf(Pair(-1, -1)) }

        var usernameSupportingText by remember { mutableStateOf("") }
        var usernameIsError by remember { mutableStateOf(false) }

        var password by remember { mutableStateOf("") }
        var confirmPassword by remember { mutableStateOf("") }
        var confirmPasswordSupportingText by remember { mutableStateOf("") }
        var confirmPasswordIsError by remember { mutableStateOf(false) }


        Box {
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

                        Spacer(modifier = Modifier.height(24.dp))

                        Box {
                            Box(
                                modifier = Modifier
                                    .clickable(
                                        interactionSource = MutableInteractionSource(),
                                        indication = null
                                    ) { aviChooserShowing = true }
                            ) {
                                Avi(
                                    fg = aviSelection.first,
                                    bg = aviSelection.second,
                                    sizeDp = 150,
                                )
                            }

                            Surface(
                                shape = CircleShape,
                                shadowElevation = 3.dp,
                                modifier = Modifier
                                    .clickable(
                                        interactionSource = MutableInteractionSource(),
                                        indication = null
                                    ) { aviChooserShowing = true }
                                    .align(Alignment.BottomEnd)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_pencil),
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier
                                        .semantics { contentDescription = AVI_SELECTION_DIALOG_BUTTON }
                                        .padding(8.dp)
                                        .size(30.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(32.dp))

                        OnBoardingTextField(
                            text = username,
                            placeHolder = "Username",
                            onTextChanged = {
                                onUsernameChanged(it)
                                when {
                                    it.length > 10 -> {
                                        usernameSupportingText = "${it.length}/10"
                                        usernameIsError = true
                                    }

                                    it.contains(Regex("\\s+")) -> {
                                        usernameSupportingText = "whitespaces not allowed"
                                        usernameIsError = true
                                    }

                                    it.isBlank() -> {
                                        usernameSupportingText = ""
                                        usernameIsError = false
                                    }

                                    it.length < 3 -> {
                                        usernameSupportingText = "at least 3 characters"
                                        usernameIsError = true
                                    }

                                    else -> {
                                        usernameSupportingText = "${it.length}/10"
                                        usernameIsError = false
                                    }
                                }
                            },
                            isError = usernameIsError,
                            supportingText = usernameSupportingText
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        OnBoardingTextField(
                            text = password,
                            placeHolder = "Password",
                            onTextChanged = {
                                password = it

                                if (password != confirmPassword) {
                                    confirmPasswordSupportingText = "passwords do not match"
                                    confirmPasswordIsError = true
                                } else {
                                    confirmPasswordSupportingText = ""
                                    confirmPasswordIsError = false
                                }
                            },
                            forPassword = true
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        OnBoardingTextField(
                            text = confirmPassword,
                            placeHolder = "Confirm Password",
                            onTextChanged = {
                                confirmPassword = it

                                if (password != confirmPassword) {
                                    confirmPasswordSupportingText = "passwords do not match"
                                    confirmPasswordIsError = true
                                } else {
                                    confirmPasswordSupportingText = ""
                                    confirmPasswordIsError = false
                                }
                            },
                            forPassword = true,
                            isError = confirmPasswordIsError,
                            supportingText = confirmPasswordSupportingText
                        )

                        Spacer(modifier = Modifier.height(64.dp))

                        FilledTonalButton(
                            onClick = {
                                register(
                                    username,
                                    password,
                                    aviSelection.first,
                                    aviSelection.second
                                )
                            },
                            enabled = !usernameIsError
                                    && !confirmPasswordIsError
                                    && username.isNotBlank()
                                    && password.isNotBlank()
                                    && aviSelection.first != -1
                                    && aviSelection.second != -1
                                    && registrationStatus != OnBoardingOpStatus.BUSY,
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(vertical = 16.dp),
                            colors = ButtonDefaults.filledTonalButtonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            modifier = Modifier.fillMaxWidth()

                        ) {
                            AnimatedContent(
                                targetState = registrationStatus,
                                label = "button animation"
                            ) {
                                if (it != OnBoardingOpStatus.BUSY) {
                                    Text(
                                        text = "Register",
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
                                text = "They say you're a typing wizard.",
                                style = MaterialTheme.typography.bodyMedium
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Image(
                                painter = painterResource(R.drawable.wizard_emoji),
                                contentDescription = null,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    }

                    AnimatedVisibility(aviChooserShowing) {
                        Box(
                            modifier = Modifier.padding(horizontal = 16.dp)
                        ) {
                            AviChooserDialog(
                                onConfirmSelection = { fg, bg ->
                                    aviSelection = Pair(fg, bg)
                                },
                                onClose = { aviChooserShowing = false }
                            )
                        }
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true, widthDp = 412, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun PrevRegisterScreen() {
    OziComposeTheme {
        RegisterScreen_simple(
            username = "",
            onUsernameChanged = { },
            registrationStatus = OnBoardingOpStatus.DEFAULT,
            { _, _, _, _ -> }
        )
    }
}

