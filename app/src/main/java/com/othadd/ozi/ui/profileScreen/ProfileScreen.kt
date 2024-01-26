package com.othadd.ozi.ui.profileScreen

import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.othadd.ozi.common.BUSY
import com.othadd.ozi.common.FAILED
import com.othadd.ozi.common.PASSED
import com.othadd.ozi.domain.model.OziData
import com.othadd.ozi.domain.model.User
import com.othadd.ozi.ui.Avi
import com.othadd.ozi.ui.AviChooserDialog
import com.othadd.ozi.ui.TopBar
import com.othadd.ozi.ui.theme.OziComposeTheme
import com.othadd.ozi.testUser5
import com.othadd.oziX.R

@Composable
fun ProfileScreen(
    goBack: () -> Unit,
    setUiReady: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var user: User? by remember { mutableStateOf(null) }
    val context = LocalContext.current
    var usernameIsEditable by remember { mutableStateOf(false) }

    ProfileScreen_simple(
        user = user,
        fetchingData = uiState.userData is OziData.Fetching,
        updating = uiState.updateStatus == BUSY,
        update = { username, aviFg, aviBg -> viewModel.update(username, aviFg, aviBg) },
        onBackArrowPressed = goBack,
        usernameIsEditable = usernameIsEditable,
        updateUsernameIsEditable = { usernameIsEditable = it }
    )

    LaunchedEffect(key1 = uiState) {
        if (uiState.userData.data != null) {
            user = uiState.userData.data
        }

        if (uiState.userData is OziData.Error) {
            Toast.makeText(context, "Could not refresh profile", Toast.LENGTH_LONG).show()
        }

        if (uiState.updateStatus == FAILED) {
            Toast.makeText(context, "Profile update failed. Check network", Toast.LENGTH_LONG)
                .show()
        }

        if (uiState.updateStatus == PASSED) {
            Toast.makeText(context, "Profile updated successfully", Toast.LENGTH_LONG).show()
            usernameIsEditable = false
        }
    }

    LaunchedEffect(key1 = true) {
        viewModel.refresh()
        setUiReady()
    }
}

@Composable
fun ProfileScreen_simple(
    user: User?,
    fetchingData: Boolean,
    updating: Boolean,
    update: (String, Int, Int) -> Unit,
    onBackArrowPressed: () -> Unit,
    usernameIsEditable: Boolean,
    updateUsernameIsEditable: (Boolean) -> Unit
) {

    var aviChooserShowing by remember { mutableStateOf(false) }
    var username by remember(user) { mutableStateOf(user?.username ?: "") }
    var usernameSupportingText by remember { mutableStateOf("") }
    var usernameIsError by remember { mutableStateOf(false) }
    var aviSelection by remember(user) {
        mutableStateOf(
            Pair(
                user?.aviFg ?: -1,
                user?.aviBg ?: -1
            )
        )
    }
    var shouldEnableUpdateButton by remember { mutableStateOf(false) }

    Surface(
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {

                Surface(
                    color = MaterialTheme.colorScheme.surface
                ) {
                    TopBar(
                        title = "Profile",
                        onBackClicked = { onBackArrowPressed() }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Box {
                    Box(
                        modifier = Modifier
                            .clickable(
                                interactionSource = MutableInteractionSource(),
                                indication = null
                            ) { aviChooserShowing = true }
                            .size(150.dp)
                    ) {
                        Avi(
                            fg = aviSelection.first,
                            bg = aviSelection.second,
                            sizeDp = 150
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
                                .padding(8.dp)
                                .size(30.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = username,
                    onValueChange = {
                        username = it

                        when {
                            !usernameIsEditable -> {
                                usernameSupportingText = ""
                                usernameIsError = false
                            }

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

                            it.length in 1..10 -> {
                                usernameSupportingText = "${it.length}/10"
                                usernameIsError = false
                            }
                        }
                    },
                    isError = usernameIsError,
                    supportingText = {
                        Text(
                            text = usernameSupportingText,
                            style = MaterialTheme.typography.labelMedium
                        )
                    },
                    label = {
                        Text(text = "Username")
                    },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_person),
                            contentDescription = null
                        )
                    },
                    trailingIcon = {
                        Icon(
                            painter = painterResource(id = if (usernameIsEditable) R.drawable.ic_undo else R.drawable.ic_pencil),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .clickable {
                                    if (usernameIsEditable) {
                                        username = user!!.username
                                        usernameSupportingText = ""
                                        usernameIsError = false
                                    }
                                    updateUsernameIsEditable(!usernameIsEditable)
                                }
                                .size(20.dp)
                        )
                    },
                    enabled = usernameIsEditable,
                    readOnly = updating
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = if (user?.verified == true) "true" else "false",
                    onValueChange = { },
                    label = {
                        Text(text = "Verified")
                    },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_verified),
                            contentDescription = null
                        )
                    },
                    enabled = false
                )

                Spacer(modifier = Modifier.height(32.dp))

                if (shouldEnableUpdateButton && !usernameIsError) {
                    ElevatedButton(
                        onClick = { update(username, aviSelection.first, aviSelection.second) },
                        shape = RoundedCornerShape(8.dp),
                    ) {
                        AnimatedContent(
                            targetState = updating,
                            label = "update button busy animation"
                        ) {
                            if (it) {
                                CircularProgressIndicator(
                                    strokeWidth = 2.dp,
                                    strokeCap = StrokeCap.Round,
                                    modifier = Modifier.size(20.dp)
                                )
                            } else {
                                Text(text = "Update")
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            if (fetchingData) {
                CircularProgressIndicator(
                    //                progress = 0.9f,
                    strokeWidth = 3.dp,
                    strokeCap = StrokeCap.Round,
                    modifier = Modifier
                        .padding(top = 12.dp, end = 16.dp)
                        .align(Alignment.TopEnd)
                        .size(25.dp)
                )
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

    LaunchedEffect(key1 = aviSelection, key2 = user, key3 = username) {
        if (user != null) {
            shouldEnableUpdateButton =
                aviSelection.first != user.aviFg || aviSelection.second != user.aviBg || username != user.username
        }
    }
}

@Preview(widthDp = 412)
@Composable
fun PrevProfileScreen() {
    OziComposeTheme {
        ProfileScreen_simple(
            user = testUser5,
            fetchingData = true,
            updating = true,
            update = { _, _, _ -> },
            onBackArrowPressed = { },
            usernameIsEditable = false,
            updateUsernameIsEditable = { }
        )
    }
}