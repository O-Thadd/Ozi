package com.othadd.ozi.ui.chat

import androidx.lifecycle.SavedStateHandle
import com.othadd.ozi.common.derivePairChatId
import com.othadd.ozi.domain.model.chat.Chat
import com.othadd.ozi.domain.useCases.interfaces.chat.GetSingleChatUseCase
import com.othadd.ozi.domain.useCases.interfaces.message.GetMessagesUseCase
import com.othadd.ozi.domain.useCases.interfaces.message.MarkMessagesReadUseCase
import com.othadd.ozi.domain.useCases.interfaces.message.SendMessageUseCase
import com.othadd.ozi.domain.useCases.interfaces.user.GetUsersUseCases
import com.othadd.ozi.domain.useCases.interfaces.user.ThisUserUseCases
import com.othadd.ozi.testChatItems
import com.othadd.ozi.testUser1
import com.othadd.ozi.testUser2
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class ChatViewModelTest {

    private val participants = listOf(testUser1, testUser2)
    private val chatId = derivePairChatId(testUser1.userId, testUser2.userId)
    private val chat = Chat(chatId, participants.map { it.userId }, false)

    private lateinit var sendMessageUseCase: SendMessageUseCase
    private lateinit var markMessagesReadUseCase: MarkMessagesReadUseCase
    private lateinit var getSingleChatUseCase: GetSingleChatUseCase
    private lateinit var getUsersUseCases: GetUsersUseCases
    private lateinit var getMessagesUseCase: GetMessagesUseCase
    private lateinit var thisUserUseCases: ThisUserUseCases
    private lateinit var savedStateHandle: SavedStateHandle

    private lateinit var chatViewModel: ChatViewModel

    private val testDispatcher = StandardTestDispatcher()

    private fun setUpViewModel() {
        sendMessageUseCase = mock()
        markMessagesReadUseCase = mock()
        getSingleChatUseCase = mock {
            onBlocking { invoke(chat.chatId) } doReturn flowOf(chat)
        }
        getUsersUseCases = mock {
            onBlocking { getUserFlow(testUser2.userId) } doReturn flowOf(testUser2)
        }
        getMessagesUseCase = mock {
            onBlocking { invoke(chatId) } doReturn flowOf(testChatItems)
        }
        thisUserUseCases = mock {
            onBlocking { get() } doReturn flowOf(testUser1)
        }
        savedStateHandle = mock {
            on { get<String>(CHAT_ID_KEY) } doReturn chat.chatId
        }

        chatViewModel =
            ChatViewModel(
                sendMessageUseCase = sendMessageUseCase,
                markMessagesReadUseCase = markMessagesReadUseCase,
                getSingleChatUseCase = getSingleChatUseCase,
                getUsersUseCases = getUsersUseCases,
                getMessagesUseCase = getMessagesUseCase,
                thisUserUseCases = thisUserUseCases,
                savedStateHandle = savedStateHandle
            )
    }

    @Before
    fun setUp() = runBlocking {
        Dispatchers.setMain(testDispatcher)
        setUpViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun chatViewModel_uiState() = runTest {
        val expectedUiState = ChatUiState(
            chatId,
            testUser2.username,
            testUser2.aviFg,
            testUser2.aviBg,
            testUser2.verified,
            testChatItems
        )
        assertEquals(expectedUiState, chatViewModel.uiState.take(2).last())
    }

    @Test
    fun chatViewModel_sendMessage() = runTest {
        val messageBody = "testMessageBody"
        chatViewModel.sendMessage(messageBody)
        launch { verify(sendMessageUseCase).invoke(messageBody, chatId) }
    }

    @Test
    fun chatViewModel_marMessagesRead() = runTest {
        chatViewModel.markMessagesRead()
        launch { verify(markMessagesReadUseCase).invoke(chatId) }
    }

    @Test
    fun chatViewModel_getParticipants() = runTest {
        val retrievedParticipantIds = async { chatViewModel.getParticipantsIds() }.await()
        launch { verify(getSingleChatUseCase).invoke(chatId) }
        assertEquals(listOf(testUser2.userId), retrievedParticipantIds)
    }
}