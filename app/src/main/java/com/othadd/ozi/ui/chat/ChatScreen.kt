package com.othadd.ozi.ui.chat

import android.content.res.Configuration
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.othadd.ozi.common.GROUP_CHAT_NAME
import com.othadd.ozi.domain.model.message.ChatItem
import com.othadd.ozi.domain.model.message.ChatItemType
import com.othadd.ozi.domain.model.message.MessageSender
import com.othadd.ozi.domain.model.message.TimeStamp
import com.othadd.ozi.domain.model.message.UIMessage
import com.othadd.ozi.ui.Avi
import com.othadd.ozi.ui.OziTextField
import com.othadd.ozi.chatItems
import com.othadd.ozi.ui.getAviBGColorDark
import com.othadd.ozi.ui.getAviBGColorLight
import com.othadd.ozi.message1
import com.othadd.ozi.ui.theme.OziComposeTheme
import com.othadd.ozi.ui.model.Destination
import com.othadd.ozi.testUIChat
import com.othadd.oziX.R
import kotlinx.coroutines.launch


@Composable
fun ChatScreen(
    updateCurrentDestination: (String) -> Unit,
    confirmSendGameRequest: (List<String>, String) -> Unit,
    gameModeratorId: String?,
    goBack: () -> Unit,
) {
    val viewModel: ChatViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isGameChat = remember(uiState.chatId.length) { uiState.chatId.length <= 40 }
    val showInputLayout =
        remember(isGameChat, gameModeratorId) { !isGameChat || gameModeratorId != null }

    ChatScreen_simple(
        uiState = uiState,
        showInputLayout = showInputLayout,
        goBack = goBack,
        sendMessage = { viewModel.sendMessage(it) },
        confirmSendGameRequest = confirmSendGameRequest,
        getParticipantsIds = { viewModel.getParticipantsIds() }
    )

    LaunchedEffect(key1 = true) {
        updateCurrentDestination(Destination.CHAT.route)
    }

    DisposableEffect(key1 = true) {
        onDispose {
            viewModel.markMessagesRead()
        }
    }
}

@Composable
fun ChatScreen_simple(
    uiState: ChatUiState,
    showInputLayout: Boolean,
    goBack: () -> Unit,
    sendMessage: (String) -> Unit,
    confirmSendGameRequest: (List<String>, String) -> Unit,
    getParticipantsIds: suspend () -> List<String>
) {
    val coroutineScope = rememberCoroutineScope()

    Surface(
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            TopBar(
                chatmateUsername = uiState.chatName,
                fg = uiState.aviFg,
                bg = uiState.aviBg,
                verified = uiState.verified,
                onBackClicked = goBack
            )

            Spacer(modifier = Modifier.height(30.dp))

            Box(
                modifier = Modifier.weight(1f)
            ) {
                ChatItems(
                    chatItems = uiState.chatItems,
                    forGroup = uiState.chatId.length < 40
                )
            }

            if (showInputLayout) {
                Box(
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    InputLayout(
                        onSendClicked = { sendMessage(it) },
                        onGameClicked = {
                            if (uiState.chatName != GROUP_CHAT_NAME) {
                                coroutineScope.launch {
                                    confirmSendGameRequest(
                                        getParticipantsIds(),
                                        uiState.chatName
                                    )
                                }
                            }
                        },
                        gameMode = uiState.chatId.length <= 40
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 412, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun PrevChatScreen() {
    OziComposeTheme {
        ChatScreen_simple(
            confirmSendGameRequest = { _, _ -> },
            showInputLayout = true,
            goBack = { },
            uiState = testUIChat,
            sendMessage = { },
            getParticipantsIds = { emptyList() }
        )
    }
}


@Composable
fun InputLayout(
    onSendClicked: (String) -> Unit,
    onGameClicked: () -> Unit,
    gameMode: Boolean
) {
    var text by remember { mutableStateOf("") }

    OziTextField(
        text = text,
        onTextChanged = { text = it },
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_game),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .clickable {
                        onGameClicked()
                    }
                    .size(36.dp)
            )
        },
        placeHolder = {
            Text(
                text = "Message...",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        },
        trailingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_app_icon),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .clickable {
                        if (text.isNotBlank()) {
                            onSendClicked(text)
                            text = ""
                        }
                    }
                    .size(36.dp)
            )
        },
        suggestions = !gameMode
    )
}

//@Preview(showBackground = true, widthDp = 412, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun PrevInputLayoutX() {
    OziComposeTheme {
        InputLayout(
            onSendClicked = { },
            onGameClicked = { },
            gameMode = false
        )
    }
}


@Composable
fun TopBar(
    chatmateUsername: String,
    fg: Int,
    bg: Int,
    verified: Boolean,
    onBackClicked: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_back_arrow),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .clickable { onBackClicked() }
                .align(Alignment.CenterStart)
        )

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.align(Alignment.Center)
        ) {
            Avi(
                fg = fg,
                bg = bg,
                modifier = Modifier.size(50.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = chatmateUsername,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )

            if (verified) {
                Spacer(modifier = Modifier.width(8.dp))

                Icon(
                    painter = painterResource(id = R.drawable.ic_verified),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}

//@Preview(showBackground = true, widthDp = 412, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PrevChatTopBar() {
    OziComposeTheme {
        TopBar(
            chatmateUsername = "ABCDEFGHIJ",
            fg = 13,
            bg = 1,
            verified = true,
            onBackClicked = { }
        )
    }
}


@Composable
fun SelfMessage(text: String) {
    Surface(
        shape = RoundedCornerShape(8.dp, 8.dp, 0.dp, 8.dp),
        color = MaterialTheme.colorScheme.primary
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(8.dp)
        )
    }
}

//@Preview(showBackground = true, widthDp = 412)
@Composable
fun PrevSelfMessage() {
    OziComposeTheme {
        SelfMessage(message1.body)
    }
}


@Composable
fun ModeratorMessage(text: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Divider()
        Text(
            text = text,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(8.dp)
        )
        Divider()
    }
}


@Composable
fun MateMessage(
    text: String,
    senderName: String? = null,
    aviBgColor: Int? = null
) {
    Surface(
        shape = RoundedCornerShape(8.dp, 8.dp, 8.dp, 0.dp),
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            senderName?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp),
                    color = if (isSystemInDarkTheme())
                        getAviBGColorLight(aviBgColor!!)
                    else
                        getAviBGColorDark(aviBgColor!!)
                )
                Spacer(modifier = Modifier.height(4.dp))
            }
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun PrevMateMessage() {
    OziComposeTheme {
        MateMessage(
            text = "How are ya",
            senderName = "Isaca"
        )
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChatItems(
    chatItems: List<ChatItem>,
    forGroup: Boolean = false
) {
    val state = rememberLazyListState()

    LaunchedEffect(key1 = chatItems) {
        if (state.firstVisibleItemIndex == 0) {
            state.animateScrollToItem(0)
        }
    }

    LazyColumn(
        state = state,
        reverseLayout = true,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {

        for (chatItem in chatItems.reversed()) {

            when (chatItem.type) {
                ChatItemType.MESSAGE -> {
                    chatItem as UIMessage

                    when (chatItem.senderType) {
                        MessageSender.SELF -> {
                            item {
                                Box(
                                    contentAlignment = Alignment.CenterEnd,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .animateItemPlacement()
                                ) {
                                    Box(
                                        contentAlignment = Alignment.CenterEnd,
                                        modifier = Modifier
                                            .fillMaxWidth(0.8f)
                                    ) {
                                        SelfMessage(text = chatItem.body)
                                    }
                                }
                            }
                        }

                        MessageSender.CHATMATE -> {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(0.8f)
                                        .animateItemPlacement()
                                ) {
                                    if (forGroup) {
                                        MateMessage(
                                            text = chatItem.body,
                                            senderName = chatItem.sender?.username,
                                            aviBgColor = chatItem.sender?.aviBg
                                        )
                                    } else {
                                        MateMessage(text = chatItem.body)
                                    }
                                }
                            }
                        }

                        MessageSender.SYSTEM -> {}


                        MessageSender.GAME_MODERATOR -> {
                            item {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .animateItemPlacement()
                                ) {
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier
                                            .fillMaxWidth(0.6f)
                                    ) {
                                        ModeratorMessage(text = chatItem.body)
                                    }
                                }
                            }
                        }
                    }
                }

                ChatItemType.DATE -> {
                    item {
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .animateItemPlacement()
                        ) {
                            Box(Modifier.align(Alignment.Center)) {
                                Date(date = chatItem.body)
                            }
                        }
                    }
                    item { Spacer(modifier = Modifier.height(16.dp)) }
                }

                ChatItemType.TIMESTAMP -> {
                    chatItem as TimeStamp

                    item { Spacer(modifier = Modifier.height(8.dp)) }

                    when (chatItem.sender) {
                        MessageSender.SELF -> {
                            item {
                                Box(
                                    Modifier
                                        .fillMaxWidth()
                                        .animateItemPlacement()
                                ) {
                                    Box(Modifier.align(Alignment.CenterEnd)) {
                                        Timestamp(chatItem)
                                    }
                                }
                            }
                        }

                        MessageSender.CHATMATE ->
                            item {
                                Box(
                                    modifier = Modifier.animateItemPlacement()
                                ) {
                                    Timestamp(chatItem)
                                }
                            }

                        MessageSender.SYSTEM -> {}

                        MessageSender.GAME_MODERATOR -> {}
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 412, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun PrevChatItems() {
    OziComposeTheme {
        ChatItems(
            chatItems = chatItems,
            forGroup = true
        )
    }
}


@Composable
fun Timestamp(timestamp: TimeStamp) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = timestamp.body,
            style = MaterialTheme.typography.labelSmall
        )

        if (timestamp.sender == MessageSender.SELF) {
            Spacer(modifier = Modifier.width(4.dp))
            if (timestamp.sent) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_sent),
                    contentDescription = null,
                    modifier = Modifier.size(14.dp)
                )
            } else {
                Icon(
                    painter = painterResource(id = R.drawable.ic_sending),
                    contentDescription = null,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}

@Composable
fun Date(date: String) {
    Text(
        text = date,
        style = MaterialTheme.typography.labelLarge
    )
}

