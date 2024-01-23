package com.othadd.ozi.ui.home

import com.othadd.ozi.domain.useCases.interfaces.chat.DeleteChatsUseCase
import com.othadd.ozi.domain.useCases.interfaces.chat.GetChatsUseCase
import com.othadd.ozi.domain.useCases.interfaces.user.ThisUserUseCases
import com.othadd.ozi.testPairChats1
import com.othadd.ozi.testUser1
import com.othadd.ozi.ui.MainCoroutineTestRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {

    private val thisUser = testUser1
    private val chats = testPairChats1
    private lateinit var thisUserUseCases: ThisUserUseCases
    private lateinit var deleteChatsUseCase: DeleteChatsUseCase
    private lateinit var getChatsUseCase: GetChatsUseCase
    private lateinit var homeViewModel: HomeViewModel

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    private fun setUpViewModel() {
        thisUserUseCases = mock {
            on { get() } doReturn flowOf(thisUser)
        }
        deleteChatsUseCase = mock()
        getChatsUseCase = mock {
            onBlocking { invoke() } doReturn flowOf(chats)
        }

        homeViewModel = HomeViewModel(thisUserUseCases, deleteChatsUseCase, getChatsUseCase)
    }

    @get:Rule
    var mainCoroutineRule = MainCoroutineTestRule(testDispatcher)

    @Before
    fun setUp() {
        setUpViewModel()
    }

    @Test
    fun homeViewModel_uiState() = testScope.runTest {
        val expectedUiState = HomeUiState(thisUser, chats)
        launch { assertEquals(expectedUiState, homeViewModel.uiState.take(2).last()) }
    }

    @Test
    fun homeViewModel_refreshThisUser() = testScope.runTest {
        homeViewModel.refreshThisUser()
        launch { verify(thisUserUseCases).refresh() }
    }

    @Test
    fun homeViewModel_clearEmptyChats() = testScope.runTest {
        homeViewModel.clearEmptyChats()
        launch { verify(deleteChatsUseCase).deleteEmptyChats() }
    }
}