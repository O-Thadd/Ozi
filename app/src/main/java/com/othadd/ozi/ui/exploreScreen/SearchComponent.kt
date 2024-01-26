package com.othadd.ozi.ui.exploreScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.othadd.ozi.domain.model.User
import com.othadd.ozi.ui.UsersList
import com.othadd.ozi.ui.theme.OziComposeTheme
import com.othadd.ozi.testUsers1
import com.othadd.ozi.domain.model.OziData
import com.othadd.ozi.testUsernames

@Composable
fun SearchComponent(
    previousSearch: List<String>,
    searchedUsers: OziData<List<User>>,
    onSearchedUserClicked: (User, Boolean) -> Unit,
    onPrevSearchClicked: (String) -> Unit,
    onClearPrevSearch: (String) -> Unit,
    onClearAllPrevSearch: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        var searchedUsersList by remember { mutableStateOf(emptyList<User>()) }

        if (searchedUsers is OziData.Available){
            LaunchedEffect(key1 = true) {
                searchedUsersList = searchedUsers.data!!
            }
        }

        if (searchedUsers is OziData.Fetching){
            CircularProgressIndicator(
                strokeWidth = 2.dp,
                modifier = Modifier.size(18.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))
        }
        if (searchedUsersList.isNotEmpty()) {
            UsersList(
                users = searchedUsersList,
                onUserClicked = { user, selected ->
                    onSearchedUserClicked(user, selected)
                    true
                }
            )
        }

        if (previousSearch.isNotEmpty() && searchedUsersList.isEmpty()) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Recent",
                    style = MaterialTheme.typography.labelMedium
                )

                TextButton(onClick = { onClearAllPrevSearch() }) {
                    Text(
                        text = "clear all",
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            for (name in previousSearch){
                Box(
                    modifier = Modifier
                        .clickable { onPrevSearchClicked(name) }
                        .fillMaxWidth()
                ) {
                    Text(
                        text = name,
                        style = MaterialTheme.typography.labelLarge
                    )

                    Text(
                        text = "X",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .clickable { onClearPrevSearch(name) }
                            .align(Alignment.CenterEnd)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Preview
@Composable
fun PrevSearchScreen() {
    OziComposeTheme {
        SearchComponent(
            previousSearch = testUsernames,
            onSearchedUserClicked = { _, _ -> },
            onPrevSearchClicked = {  },
            onClearPrevSearch = {  },
            onClearAllPrevSearch = {  },
            searchedUsers = OziData.Available(testUsers1)
        )
    }
}