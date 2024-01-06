package com.othadd.ozi.ui

import com.othadd.ozi.domain.model.User
import com.othadd.ozi.domain.model.chat.UiChat
import com.othadd.ozi.domain.model.gaming.GamePrepOutcome
import com.othadd.ozi.domain.model.gaming.GamePrepState
import com.othadd.ozi.domain.model.gaming.UserGameState
import com.othadd.ozi.domain.model.message.ChatItem
import com.othadd.ozi.domain.model.message.ChatItemType
import com.othadd.ozi.domain.model.message.MessageSender
import com.othadd.ozi.domain.model.message.TimeStamp
import com.othadd.ozi.domain.model.message.UIMessage
import com.othadd.ozi.ui.chat.ChatUiState
import com.othadd.ozi.ui.model.DialogData
import com.othadd.ozi.ui.model.GamePrepDialogData
import com.othadd.ozi.ui.model.MenuData
import com.othadd.oziX.R

val uiTestUser1 = User(
    username = "Mr. State",
    userId = "user1Id",
    aviFg = 7,
    aviBg = 3,
    online = true,
    verified = true,
    token = "",
    gameState = UserGameState.AVAILABLE.string
)

val uiTestUser2 = User(
    username = "Francesca",
    userId = "user2Id",
    aviFg = 8,
    aviBg = 1,
    online = true,
    verified = false,
    token = "",
    gameState = UserGameState.AVAILABLE.string
)

val uiTestUser3 = User(
    username = "Alehandro",
    userId = "user3Id",
    aviFg = 9,
    aviBg = 2,
    online = false,
    verified = true,
    token = "",
    gameState = UserGameState.AVAILABLE.string
)

val uiTestUser4 = User(
    username = "montero",
    userId = "user4Id",
    aviFg = 10,
    aviBg = 3,
    online = true,
    verified = true,
    token = "",
    gameState = UserGameState.PLAYING.string
)

val uiTestUser5 = User(
    username = "Carlos",
    userId = "user5Id",
    aviFg = 11,
    aviBg = 1,
    online = false,
    verified = false,
    token = "",
    gameState = UserGameState.AVAILABLE.string
)

val uiTestUser6 = User(
    username = "Veritas",
    userId = "user6Id",
    aviFg = 12,
    aviBg = 1,
    online = true,
    verified = true,
    token = "",
    gameState = UserGameState.AVAILABLE.string
)

val uiTestUser7 = User(
    username = "Monalisa",
    userId = "user7Id",
    aviFg = 13,
    aviBg = 3,
    online = true,
    verified = false,
    token = "",
    gameState = UserGameState.AVAILABLE.string
)

val uiTestUser8 = User(
    username = "Pedro",
    userId = "user8Id",
    aviFg = 14,
    aviBg = 2,
    online = true,
    verified = false,
    token = "",
    gameState = UserGameState.AVAILABLE.string
)

val uiTestUser9 = User(
    username = "Roberto",
    userId = "user9Id",
    aviFg = 15,
    aviBg = 3,
    online = false,
    verified = false,
    token = "",
    gameState = UserGameState.AVAILABLE.string
)

val uiTestUser10 = User(
    username = "Manuel",
    userId = "user10Id",
    aviFg = 16,
    aviBg = 1,
    online = true,
    verified = true,
    token = "",
    gameState = UserGameState.AVAILABLE.string
)

val uiTestUsers1 = listOf(uiTestUser1, uiTestUser2, uiTestUser3, uiTestUser4, uiTestUser5)
val uiTestUsers2 = listOf(uiTestUser6, uiTestUser7, uiTestUser8, uiTestUser9, uiTestUser10)

val uiTestDialogTitle = "Ozi needs notification permission"
val uiTestDialogBody = "This will enable Ozi notify you of messages as soon they arrive."
val uiTestDialogButton1 = Pair("Okay", { })
val uiTestDialogButton2 = Pair("Cancel", { })

val uiTestGamePrepState = GamePrepState(
    requestCreationTime = 1699521585512 + 30000,
    host = uiTestUser3,
    thisUserId = uiTestUser3.userId,
    invitees = uiTestUsers1,
    declinedInviteesIds = listOf("user2Id"),
    acceptedInviteesIds = listOf("user5Id", uiTestUser3.userId),
    prepOutcome = GamePrepOutcome.PROMPTING_HOST
)

val uiTestDialogData = DialogData(
    title = uiTestDialogTitle,
    body = uiTestDialogBody,
    button1 = uiTestDialogButton1,
    button2 = uiTestDialogButton2
)

val uiTestGamePrepDialogData = GamePrepDialogData(
    state = uiTestGamePrepState,
    accept = { },
    decline = { },
    proceed = {  },
    cancel = {  }
)

val uiTestMenuData1 = MenuData(
    iconResourceId = R.drawable.builder,
    action = {  },
    name = "Developer"
)

val uiTestMenuData2 = MenuData(
    iconResourceId = R.drawable.ic_game,
    action = {  },
    name = "Start Game"
)

val uiTestMenuData3 = MenuData(
    name = "Group Chats",
    action = { },
)

val uiTestMenuItems = listOf(uiTestMenuData1, uiTestMenuData2, uiTestMenuData3)

val uiTestChat1 = UiChat.PairChat(
    "chat1",
    "Dimitri",
    "Alright. I'll check and get back to you. So I need to know what you think now",
    0,
    "yesterday",
    7,
    2,
    true,
    true
)

val uiTestChat2 = UiChat.PairChat(
    "chat2",
    "Festus",
    "Whatsup? seen it yet?",
    0,
    "22/09/23",
    8,
    2,
    true,
    false
)

val uiTestChat3 = UiChat.PairChat(
    "chat3",
    "Dan",
    "Haq Haq, shey i been tell you",
    0,
    "22/09/23",
    9,
    1,
    false,
    true
)

val uiTestChat4 = UiChat.PairChat(
    "chat4",
    "Erasmus",
    "I'll someone time.",
    0,
    "02/09/23",
    10,
    3,
    false,
    false
)

val uiTestPairChats1 = listOf(uiTestChat1, uiTestChat2, uiTestChat3, uiTestChat4)

val uiTestUsernames = listOf("lorenzo", "mater", "Benedict", "pascal")

val uiTestFreshUser1 = User(
    username = "Carmala",
    userId = "freshUser1Id",
    aviFg = 11,
    aviBg = 1,
    online = true,
    verified = false,
    token = "",
    gameState = UserGameState.AVAILABLE.string
)

val uiTestFreshUser2 = User(
    username = "Furio",
    userId = "freshUser2Id",
    aviFg = 12,
    aviBg = 2,
    online = true,
    verified = false,
    token = "",
    gameState = UserGameState.AVAILABLE.string
)

val date1 = ChatItem("Tuesday, 17.05.2023", ChatItemType.DATE)

val message1 = UIMessage(
    "",
    null,
    MessageSender.SELF,
    "Hey, so what do you think?",
    "7:30 pm",
    true
)

val message2 = UIMessage(
    "",
    null,
    MessageSender.SELF,
    "Where the wizard king, Inioch sat upon his throne and maintained a delicate balance of light shadow",
    "7:30 pm",
    false
)

val timeStamp1 = TimeStamp(message2.time, message2.sent, message2.senderType)

val message3 = UIMessage(
    "",
    uiTestFreshUser2,
    MessageSender.CHATMATE,
    "Yep!",
    "4:30 pm",
    true
)

val timeStamp2 = TimeStamp(message3.time, message3.sent, message3.senderType)

val date2 = ChatItem("Wednesday, 18.05.2023", ChatItemType.DATE)

val message4 = UIMessage(
    "",
    uiTestFreshUser1,
    MessageSender.CHATMATE,
    "Seems legit o",
    "4:31 pm",
    true
)

val message5 = UIMessage(
    "",
    null,
    MessageSender.GAME_MODERATOR,
//    "In the first days of the world, all living beings lived in peace, ruled by the elvenelve court in the valley of wonders",
    "send ready",
    "5:31 pm",
    true
)

val timeStamp3 = TimeStamp(message5.time, message5.sent, message5.senderType)

val date3 = ChatItem("Friday, 20.05.2023", ChatItemType.DATE)

val message6 = UIMessage(
    "",
    null,
    MessageSender.SELF,
    "Alright na.",
    "5:31 pm",
    true
)

val timeStamp4 = TimeStamp(message6.time, message6.sent, message6.senderType)

val chatItems = listOf(
    date1,
    message1,
    message2,
    timeStamp1,
    message3,
    timeStamp2,
    date2,
    message4,
    timeStamp3,
    message5,
    date3,
    message6,
    timeStamp4
)

val uiTestUIChat = ChatUiState(
    chatId = "1234567890",
    chatName = uiTestUser1.username,
    aviFg = uiTestUser1.aviFg,
    aviBg = uiTestUser1.aviBg,
    verified = uiTestUser1.verified,
    chatItems = chatItems
)