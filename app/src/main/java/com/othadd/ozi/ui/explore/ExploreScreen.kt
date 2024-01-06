package com.othadd.ozi.ui.explore

import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
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
import com.othadd.ozi.ui.MainActivity
import com.othadd.ozi.ui.OziTextField
import com.othadd.ozi.ui.UsersList
import com.othadd.ozi.ui.theme.OziComposeTheme
import com.othadd.ozi.ui.uiTestUsers1
import com.othadd.ozi.ui.uiTestUsers2
import com.othadd.ozi.domain.model.OziData
import com.othadd.ozi.domain.model.User
import com.othadd.ozi.ui.TopBar
import com.othadd.ozi.ui.model.Destination
import com.othadd.ozi.ui.uiTestUsernames
import com.othadd.oziX.R
import kotlinx.coroutines.launch

@Composable
fun ExploreScreen(
    onUserClicked: (chatId: String) -> Unit,
    onBackClicked: () -> Unit,
    updateCurrentDestination: (String) -> Unit,
) {
    val viewModel: ExploreViewmodel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(key1 = uiState) {
        if (uiState.exploredUsers is OziData.Error || uiState.searchedUsers is OziData.Error) {
            Toast.makeText(context, "Could not fetch users", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(key1 = true) {
        viewModel.getExploreUsers()
        updateCurrentDestination(Destination.EXPLORE.route)
    }

    ExploreScreen_simple(
        exploredUsers = uiState.exploredUsers,
        onExploreUserClicked = { user, _ ->
            coroutineScope.launch {
                val chatId = viewModel.createChat(user = user)
                onUserClicked(chatId)
            }
        },
        onBackClicked = onBackClicked,
        search = { viewModel.searchUsers(it) },
        previousSearches = uiState.prevSearches,
        searchedUsers = uiState.searchedUsers,
        onSearchedUserClicked = { user, _ ->
            coroutineScope.launch {
                viewModel.addToPrevSearches(user.username)
                val chatId = viewModel.createChat(user = user)
                onUserClicked(chatId)
            }
        },
        onPrevSearchClicked = { viewModel.searchUsers(it) },
        onClearPrevSearch = { viewModel.removePrevSearch(it) },
        onClearAllPreviousSearch = { viewModel.clearPrevSearches() }
    )
}

@Composable
private fun ExploreScreen_simple(
    exploredUsers: OziData<List<User>>,
    onExploreUserClicked: (User, Boolean) -> Unit,
    onBackClicked: () -> Unit,
    search: (String) -> Unit,
    previousSearches: List<String>,
    searchedUsers: OziData<List<User>>,
    onSearchedUserClicked: (User, Boolean) -> Unit,
    onPrevSearchClicked: (String) -> Unit,
    onClearPrevSearch: (String) -> Unit,
    onClearAllPreviousSearch: () -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.background
    ) {

        var inSearchMode by rememberSaveable { mutableStateOf(false) }

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Surface(
                color = MaterialTheme.colorScheme.surface
            ) {
                TopBar(
                    title = "Explore",
                    onBackClicked = onBackClicked
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                var text by rememberSaveable { mutableStateOf("") }

                OziTextField(
                    text = text,
                    onTextChanged = {
                        text = it
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
                            text = "search for someone",
                            style = MaterialTheme.typography.labelLarge
                        )
                    },
                    trailingIcon = { },
                    onClicked = {
                        inSearchMode = true
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                AnimatedContent(
                    targetState = inSearchMode,
                    label = "animate search mode",
                    transitionSpec = {
                        if (targetState) {
                            slideInVertically { height -> height } togetherWith
                                    slideOutVertically { height -> -height }
                        } else {
                            slideInVertically() togetherWith slideOutVertically()
                        }
                    }
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        if (it) {
                            BackHandler(enabled = inSearchMode) {
                                inSearchMode = false
                            }

                            SearchComponent(
                                previousSearch = previousSearches,
                                onSearchedUserClicked = onSearchedUserClicked,
                                onPrevSearchClicked = onPrevSearchClicked,
                                onClearPrevSearch = onClearPrevSearch,
                                onClearAllPrevSearch = onClearAllPreviousSearch,
                                searchedUsers = searchedUsers
                            )
                        } else {
                            Column {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "suggested",
                                        style = MaterialTheme.typography.labelLarge
                                    )

                                    if (exploredUsers is OziData.Fetching) {
                                        Spacer(modifier = Modifier.width(8.dp))

                                        CircularProgressIndicator(
                                            strokeWidth = 2.dp,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                if (exploredUsers is OziData.Available) {
                                    UsersList(
                                        users = exploredUsers.data!!,
                                        onUserClicked = { user, selected ->
                                            onExploreUserClicked(user, selected)
                                            true
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(widthDp = 412)
@Composable
fun PrevExploreScreen() {
    OziComposeTheme {
        ExploreScreen_simple(
            exploredUsers = OziData.Available(uiTestUsers1),
            onExploreUserClicked = { _, _ -> },
            onBackClicked = { },
            search = { },
            previousSearches = uiTestUsernames,
            searchedUsers = OziData.Available(uiTestUsers2),
            onSearchedUserClicked = { _, _ -> },
            onPrevSearchClicked = { _ -> },
            onClearPrevSearch = { _ -> },
            onClearAllPreviousSearch = { }
        )
    }
}