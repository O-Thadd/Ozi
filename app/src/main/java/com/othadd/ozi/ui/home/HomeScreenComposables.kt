package com.othadd.ozi.ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.othadd.ozi.common.HOME_CHAT_ITEM
import com.othadd.ozi.common.THIS_USER_AVI
import com.othadd.ozi.domain.model.chat.UiChat
import com.othadd.ozi.domain.model.User
import com.othadd.ozi.ui.Avi
import com.othadd.ozi.ui.MenuButtonIcon
import com.othadd.ozi.ui.ThemeState
import com.othadd.ozi.ui.ThemeSwitch
import com.othadd.ozi.ui.VerifiedIcon
import com.othadd.ozi.ui.theme.OziComposeTheme
import com.othadd.ozi.testChat1
import com.othadd.ozi.testPairChats1
import com.othadd.ozi.testUser1

@Composable
fun HomeScreenTopBar(
    thisUser: User,
    onAviPressed: () -> Unit,
    onMenuButtonPressed: () -> Unit,
    currentTheme: ThemeState,
    switchTheme: () -> Unit,
    modifier: Modifier
) {
    Surface(
        color = MaterialTheme.colorScheme.background,
        tonalElevation = 3.dp
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .semantics { contentDescription = THIS_USER_AVI }
                    .clickable { onAviPressed() }
                    .size(60.dp)
            ) {
                Avi(
                    fg = thisUser.aviFg,
                    bg = thisUser.aviBg,
                    sizeDp = 60
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = thisUser.username,
                style = MaterialTheme.typography.headlineLarge,
            )

            Spacer(modifier = Modifier.width(8.dp))

            if (thisUser.verified) {
                VerifiedIcon()
            }

            Box(
                contentAlignment = Alignment.CenterEnd,
                modifier = Modifier.weight(1f)
            ) {
                ThemeSwitch(
                    onPressed = switchTheme,
                    state = currentTheme
                )
            }

            Spacer(modifier = Modifier.width(18.dp))

            MenuButtonIcon {
                onMenuButtonPressed()
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 412)
@Composable
fun PrevTopBar() {
    OziComposeTheme {
        HomeScreenTopBar(
            thisUser = testUser1,
            onAviPressed = { },
            onMenuButtonPressed = { },
            currentTheme = ThemeState.LIGHT,
            switchTheme = { },
            modifier = Modifier,
        )
    }
}

@Composable
fun Chat(chat: UiChat, onClick: (userId: String) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .semantics { contentDescription = "$HOME_CHAT_ITEM ${chat.chatName}" }
            .clickable { onClick(chat.chatId) }
    ) {
        Avi(fg = chat.aviFg, bg = chat.aviBg, sizeDp = 40)

        Spacer(modifier = Modifier.width(8.dp))

        Column(
            Modifier.weight(1f)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = chat.chatName,
                    style = MaterialTheme.typography.bodyLarge
                )

                if (chat is UiChat.PairChat && chat.verified) {
                    Spacer(modifier = Modifier.width(4.dp))
                    VerifiedIcon()
                }
            }
            Text(
                text = chat.lastMessage,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            horizontalAlignment = Alignment.End
        ) {
            if (chat.hasUnreadMessage) {
                Surface(
                    shape = CircleShape,
                    color = Color.Green,
                    modifier = Modifier.size(8.dp)
                ) { }

                Spacer(modifier = Modifier.height(8.dp))
            }

            Text(
                text = chat.lastMessageDateTime,
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}


@Preview(showBackground = true, widthDp = 412)
@Composable
fun PrevChat() {
    OziComposeTheme {
        Chat(testChat1, { })
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Chats(
    chats: List<UiChat>,
    onChatClicked: (String) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(bottom = 70.dp),
    ) {
        items(
            items = chats,
            key = { it.chatId }
        ) {
            Box(
                modifier = Modifier.animateItemPlacement()
            ) {
                Chat(
                    chat = it,
                    onClick = onChatClicked
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 412)
@Composable
fun PrevChats() {
    OziComposeTheme {
        Chats(testPairChats1, { })
    }
}