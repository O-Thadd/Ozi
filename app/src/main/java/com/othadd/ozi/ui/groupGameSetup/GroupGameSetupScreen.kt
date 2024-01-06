package com.othadd.ozi.ui.groupGameSetup

import android.content.Context
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.othadd.ozi.domain.model.OperationOutcome
import com.othadd.ozi.domain.model.OziData
import com.othadd.ozi.domain.model.User
import com.othadd.ozi.ui.OziTextField
import com.othadd.ozi.ui.TopBar
import com.othadd.ozi.ui.User1
import com.othadd.ozi.ui.User2
import com.othadd.ozi.ui.model.Destination
import com.othadd.ozi.ui.model.DialogData
import com.othadd.ozi.ui.theme.OziComposeTheme
import com.othadd.ozi.ui.uiTestUser2
import com.othadd.ozi.ui.uiTestUser3
import com.othadd.ozi.ui.uiTestUsers1
import com.othadd.oziX.R
import kotlinx.coroutines.launch

@Composable
fun GroupGameSetupScreen(
    onBackClicked: () -> Unit,
    updateCurrentDestination: (String) -> Unit,
    postDialog: (DialogData?) -> Unit,
    postNotifyDialog: (title: String) -> Unit
) {

    val viewModel: GameSetupViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    GroupGameSetupScreen_simple(
        onBackClicked = onBackClicked,
        search = { viewModel.search(it) },
        users = uiState.users,
        onUserClicked = { user, newSelectionStatus ->
            viewModel.updateSelectedUsers(user, newSelectionStatus)
        },
        selectedUsers = uiState.selectedUsers,
        sendRequest = suspend { viewModel.sendRequest() },
        postDialog = postDialog,
        postNotifyDialog = postNotifyDialog
    )

    LaunchedEffect(key1 = true) {
        updateCurrentDestination(Destination.GROUP_GAME_SETUP.route)
    }

    LaunchedEffect(key1 = uiState.users){
        if (uiState.users is OziData.Error) {
            Toast.makeText(context, "could not fetch users", Toast.LENGTH_LONG).show()
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun GroupGameSetupScreen_simple(
    onBackClicked: () -> Unit,
    search: (String) -> Unit,
    users: OziData<List<User>>,
    selectedUsers: List<User>,
    onUserClicked: (User, Boolean) -> Boolean,
    sendRequest: suspend () -> OperationOutcome<Nothing, Nothing>,
    postDialog: (DialogData?) -> Unit,
    postNotifyDialog: (title: String) -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.background
    ) {

        var searchText by rememberSaveable { mutableStateOf("") }
        var usersList by remember { mutableStateOf(emptyList<User>()) }
        val coroutineScope = rememberCoroutineScope()
        val context = LocalContext.current

        if (users is OziData.Available) {
            LaunchedEffect(key1 = users) {
                usersList = users.data!!
            }
        }

//        if (users is OziData.Error) {
//            Toast.makeText(LocalContext.current, "could not fetch users", Toast.LENGTH_LONG).show()
//        }

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Surface(
                color = MaterialTheme.colorScheme.surface
            ) {
                TopBar(
                    title = "New Group Game",
                    subtitle = "select invitees",
                    onBackClicked = onBackClicked
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            OziTextField(
                text = searchText,
                onTextChanged = {
                    searchText = it
                    search(it)
                },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_search),
                        contentDescription = null
                    )
                },
                placeHolder = {
                    Text(
                        text = "search for anyone, even those not on your chat list",
                        style = MaterialTheme.typography.labelLarge
                    )
                },
                trailingIcon = { },
                onClicked = {
                },
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            AnimatedVisibility(
                visible = selectedUsers.isNotEmpty()
            ) {
                Column {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                    ) {
                        items(
                            items = selectedUsers,
                            key = { it.userId }
                        ) {
                            User2(
                                user = it,
                                modifier = Modifier
                                    .animateItemPlacement()
                                    .clickable { onUserClicked(it, false) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Divider()

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            AnimatedVisibility(
                visible = users is OziData.Fetching,
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Column {
                    CircularProgressIndicator(
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(18.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            ) {
                GameSetupUsersList(
                    context = context,
                    users = usersList,
                    selectedUsers = selectedUsers,
                    onUserClicked = onUserClicked,
                    selectable = true,
                    selectOnClick = true
                )
            }

            FilledTonalButton(
                shape = RoundedCornerShape(8.dp),
                onClick = {
                    coroutineScope.launch {
                        val dialogTitle =
                            if (selectedUsers.size == 1) "Sending game request..." else "Starting group game..."
                        postNotifyDialog(dialogTitle)

                        val outcome = sendRequest()

                        if (outcome is OperationOutcome.Failed) {
                            Toast.makeText(context, "Failed to start game", Toast.LENGTH_LONG)
                                .show()
                            postDialog(null)
                        }
                    }
                },
                enabled = selectedUsers.isNotEmpty(),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth(0.5f)
            ) {
                Text(
                    text = "Send Request",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun PrevGroupGameSetup() {
    OziComposeTheme {
        GroupGameSetupScreen_simple(
            onBackClicked = { },
            search = { },
            users = OziData.Available(uiTestUsers1),
//            users = OziData.Fetching(),
            selectedUsers = listOf(uiTestUser2, uiTestUser3),
//            selectedUsers = emptyList(),
            onUserClicked = { _, _ -> true },
            sendRequest = { OperationOutcome.Successful() },
            postDialog = { },
            postNotifyDialog = { }
        )
    }
}

@Composable
fun GameSetupUsersList(
    context: Context,
    users: List<User>,
    selectedUsers: List<User>? = null,
    onUserClicked: (User, Boolean) -> Boolean,
    selectable: Boolean = false,
    selectOnClick: Boolean = false
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(users) { user ->
            User1(
                user = user,
                onUserClicked = { clickedUser, selected ->
                    val successful = onUserClicked(clickedUser, selected)
                    if (!successful) {
                        Toast.makeText(
                            context,
                            "can only invite up to 5 players",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    successful
                },
                selectable = selectable,
                selected = if (selectedUsers == null) false else user in selectedUsers,
                selectOnClick = selectOnClick
            )
        }
    }
}