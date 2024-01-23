package com.othadd.ozi

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

val testUser1 = User(
    username = "Mr. State",
    userId = "deb1574b-6532-4cec-80ff-0eaa55c1212d",
    aviFg = 7,
    aviBg = 3,
    online = true,
    verified = true,
    token = "",
    gameState = UserGameState.AVAILABLE.string
)

val testUser2 = User(
    username = "Francesca",
    userId = "2fde73c2-821f-48da-b35d-09bc4f5df562",
    aviFg = 8,
    aviBg = 1,
    online = true,
    verified = false,
    token = "",
    gameState = UserGameState.AVAILABLE.string
)

val testUser3 = User(
    username = "Alehandro",
    userId = "3875d062-4620-44a5-b8c5-cf364c4b1664",
    aviFg = 9,
    aviBg = 2,
    online = false,
    verified = true,
    token = "",
    gameState = UserGameState.AVAILABLE.string
)

val testUser4 = User(
    username = "montero",
    userId = "acb1b74e-61c4-4cd2-9d4f-66a777d41469",
    aviFg = 10,
    aviBg = 3,
    online = true,
    verified = true,
    token = "",
    gameState = UserGameState.PLAYING.string
)

val testUser5 = User(
    username = "Carlos",
    userId = "21c88c24-18ec-4c08-b82c-4d7e00a33a27",
    aviFg = 11,
    aviBg = 1,
    online = false,
    verified = false,
    token = "",
    gameState = UserGameState.AVAILABLE.string
)

val testUser6 = User(
    username = "Veritas",
    userId = "526806ba-1561-418d-a838-5b51d19f0e77",
    aviFg = 12,
    aviBg = 1,
    online = true,
    verified = true,
    token = "",
    gameState = UserGameState.AVAILABLE.string
)

val testUser7 = User(
    username = "Monalisa",
    userId = "9b277f54-62db-4c10-9c73-e764ee30a356",
    aviFg = 13,
    aviBg = 3,
    online = true,
    verified = false,
    token = "",
    gameState = UserGameState.AVAILABLE.string
)

val testUser8 = User(
    username = "Pedro",
    userId = "3c86c9db-c966-41b2-9625-814933cda9d7",
    aviFg = 14,
    aviBg = 2,
    online = true,
    verified = false,
    token = "",
    gameState = UserGameState.AVAILABLE.string
)

val testUser9 = User(
    username = "Roberto",
    userId = "dd6e0e05-378b-41ae-9076-11e2513db57e",
    aviFg = 15,
    aviBg = 3,
    online = false,
    verified = false,
    token = "",
    gameState = UserGameState.AVAILABLE.string
)

val testUser10 = User(
    username = "Manuel",
    userId = "dbf934db-db7c-4986-a32d-d57b4173b05c",
    aviFg = 16,
    aviBg = 1,
    online = true,
    verified = true,
    token = "",
    gameState = UserGameState.AVAILABLE.string
)

val testUsers1 = listOf(testUser1, testUser2, testUser3, testUser4, testUser5)
val testUsers2 = listOf(testUser6, testUser7, testUser8, testUser9, testUser10)

val testDialogTitle = "Ozi needs notification permission"
val testDialogBody = "This will enable Ozi notify you of messages as soon they arrive."
val testDialogButton1 = Pair("Okay", { })
val testDialogButton2 = Pair("Cancel", { })

val testGamePrepState = GamePrepState(
    requestCreationTime = 1699521585512 + 30000,
    host = testUser3,
    thisUserId = testUser3.userId,
    invitees = testUsers1,
    declinedInviteesIds = listOf("user2Id"),
    acceptedInviteesIds = listOf("user5Id", testUser3.userId),
    prepOutcome = GamePrepOutcome.PROMPTING_HOST
)

val testDialogData = DialogData(
    title = testDialogTitle,
    body = testDialogBody,
    button1 = testDialogButton1,
    button2 = testDialogButton2
)

val testGamePrepDialogData = GamePrepDialogData(
    state = testGamePrepState,
    accept = { },
    decline = { },
    proceed = {  },
    cancel = {  }
)

val testMenuData1 = MenuData(
    iconResourceId = R.drawable.builder,
    action = {  },
    name = "Developer"
)

val testMenuData2 = MenuData(
    iconResourceId = R.drawable.ic_game,
    action = {  },
    name = "Start Game"
)

val testMenuData3 = MenuData(
    name = "Group Chats",
    action = { },
)

val testMenuItems = listOf(testMenuData1, testMenuData2, testMenuData3)

val testChat1 = UiChat.PairChat(
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

val testChat2 = UiChat.PairChat(
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

val testChat3 = UiChat.PairChat(
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

val testChat4 = UiChat.PairChat(
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

val testPairChats1 = listOf(testChat1, testChat2, testChat3, testChat4)

val testUsernames = listOf("lorenzo", "mater", "Benedict", "pascal")

val testFreshUser1 = User(
    username = "Carmala",
    userId = "freshUser1Id",
    aviFg = 11,
    aviBg = 1,
    online = true,
    verified = false,
    token = "",
    gameState = UserGameState.AVAILABLE.string
)

val testFreshUser2 = User(
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
    testFreshUser2,
    MessageSender.CHATMATE,
    "Yep!",
    "4:30 pm",
    true
)

val timeStamp2 = TimeStamp(message3.time, message3.sent, message3.senderType)

val date2 = ChatItem("Wednesday, 18.05.2023", ChatItemType.DATE)

val message4 = UIMessage(
    "",
    testFreshUser1,
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

val testChatItems = listOf(
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

val testUIChat = ChatUiState(
    chatId = "1234567890",
    chatName = testUser1.username,
    aviFg = testUser1.aviFg,
    aviBg = testUser1.aviBg,
    verified = testUser1.verified,
    chatItems = testChatItems
)