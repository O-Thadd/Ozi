package com.othadd.ozi.ui.chat

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.lifecycle.SavedStateHandle
import com.othadd.ozi.common.CHAT_INPUT_LAYOUT_GAME_BUTTON
import com.othadd.ozi.common.CHAT_INPUT_LAYOUT_SEND_BUTTON
import com.othadd.ozi.common.GO_BACK_BUTTON
import com.othadd.ozi.common.TEXT_FIELD
import com.othadd.ozi.common.derivePairChatId
import com.othadd.ozi.domain.model.chat.Chat
import com.othadd.ozi.domain.useCases.chat.FakeGetSingleChatUseCase
import com.othadd.ozi.domain.useCases.interfaces.message.MarkMessagesReadUseCase
import com.othadd.ozi.domain.useCases.message.FakeGetMessagesUseCase
import com.othadd.ozi.domain.useCases.message.FakeSendMessageUseCase
import com.othadd.ozi.domain.useCases.user.FakeGetUsersUseCases
import com.othadd.ozi.domain.useCases.user.FakeThisUseUseCases
import com.othadd.ozi.testChatItems
import com.othadd.ozi.testUser1
import com.othadd.ozi.testUser2
import com.othadd.ozi.ui.model.Destination
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.mock
import org.mockito.kotlin.spy
import org.mockito.kotlin.verify

@RunWith(MockitoJUnitRunner::class)
class ChatsScreenTest {

    private val thisUser = testUser1
    private val chatMate = testUser2
    private val participants = listOf(thisUser, chatMate)
    private val chatId = derivePairChatId(thisUser.userId, chatMate.userId)
    private val chat = Chat(chatId, participants.map { it.userId }, false)
    private val uiState = ChatUiState(
        chatId,
        chatMate.username,
        chatMate.aviFg,
        chatMate.aviBg,
        chatMate.verified,
        testChatItems
    )

    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var markMessagesReadUseCase: MarkMessagesReadUseCase
    private lateinit var confirmSendGameRequest: (List<String>, String) -> Unit
    private lateinit var goBack: () -> Unit
    private lateinit var updateCurrentDestination: (String) -> Unit

    private lateinit var viewModel: ChatViewModel

    private fun setContentOnComposeTestRule() {
        testRule.setContent {
            ChatScreen(
                updateCurrentDestination = updateCurrentDestination,
                confirmSendGameRequest = confirmSendGameRequest,
                gameModeratorId = "",
                goBack = goBack,
                viewModel = viewModel
            )
        }
    }

    @get:Rule
    var testRule = createComposeRule()

    @Before
    fun setUp() {
        savedStateHandle = SavedStateHandle().apply { set(CHAT_ID_KEY, uiState.chatId) }
        markMessagesReadUseCase = mock()
        confirmSendGameRequest = mock()
        goBack = mock()
        updateCurrentDestination = mock()

        viewModel = ChatViewModel(
            sendMessageUseCase = FakeSendMessageUseCase(),
            markMessagesReadUseCase = markMessagesReadUseCase,
            getSingleChatUseCase = FakeGetSingleChatUseCase(chat),
            getUsersUseCases = FakeGetUsersUseCases(),
            getMessagesUseCase = FakeGetMessagesUseCase(),
            thisUserUseCases = FakeThisUseUseCases(thisUser),
            savedStateHandle = savedStateHandle
        )
    }

    @Test
    fun chatScreen_updateCurrentDestinationCalled() {
        setContentOnComposeTestRule()
        verify(updateCurrentDestination).invoke(Destination.CHAT.route)
    }

    @Test
    fun chatScreen_inputLayoutShown() {
        setContentOnComposeTestRule()
        testRule.onNodeWithText("Message...").assertExists()
    }

    @Test
    fun chatScreen_backButtonPressed_goBackInvoked() {
        setContentOnComposeTestRule()
        testRule.onNodeWithContentDescription(GO_BACK_BUTTON).performClick()
        verify(goBack).invoke()
    }

    @Test
    fun chatScreen_gameButtonPressed_confirmGameRequestInvoked() {
        setContentOnComposeTestRule()
        testRule.onNodeWithContentDescription(CHAT_INPUT_LAYOUT_GAME_BUTTON).performClick()
        verify(confirmSendGameRequest).invoke(listOf(chatMate.userId), thisUser.username)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun chatScreen_sendMessage() = runTest {
        viewModel = spy(viewModel)
        setContentOnComposeTestRule()

        val textToSend = "hello world"
        testRule.onNodeWithContentDescription(TEXT_FIELD).performTextInput(textToSend)
        testRule.onNodeWithContentDescription(CHAT_INPUT_LAYOUT_SEND_BUTTON).performClick()
        verify(viewModel).sendMessage(textToSend)
    }
}