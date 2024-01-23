package com.othadd.ozi.ui.home

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.othadd.ozi.common.HOME_CHAT_ITEM
import com.othadd.ozi.common.HOME_MENU
import com.othadd.ozi.common.HOME_MENU_BUTTON
import com.othadd.ozi.common.THIS_USER_AVI
import com.othadd.ozi.domain.useCases.chat.FakeDeleteChatsUseCase
import com.othadd.ozi.domain.useCases.chat.FakeGetChatsUseCase
import com.othadd.ozi.domain.useCases.interfaces.user.ThisUserUseCases
import com.othadd.ozi.domain.useCases.user.FakeThisUseUseCases
import com.othadd.ozi.testPairChats1
import com.othadd.ozi.testUser1
import com.othadd.ozi.ui.ThemeState
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.mock
import org.mockito.kotlin.spy
import org.mockito.kotlin.verify

@RunWith(MockitoJUnitRunner::class)
class HomeScreenTest {

    private val thisUser = testUser1
    private val chats = testPairChats1

    private lateinit var goToOnBoarding: () -> Unit
    private lateinit var goToProfile: () -> Unit
    private lateinit var goToDeveloper: () -> Unit
    private lateinit var onChatClick: (String) -> Unit
    private lateinit var updateCurrentDestination: (String) -> Unit
    private lateinit var goToGroupGameSetup: () -> Unit
    private lateinit var logout: () -> Unit
    private lateinit var switchTheme: () -> Unit
    private lateinit var viewModel: HomeViewModel

    private lateinit var thisUserUseCases: ThisUserUseCases

    private fun setContentOnComposeTestRule() {
        testRule.setContent {
            HomeScreen(
                goToOnBoarding = goToOnBoarding,
                goToProfile = goToProfile,
                goToDeveloper = goToDeveloper,
                sortOutNotificationPermission = { },
                onChatClick = onChatClick,
                updateCurrentDestination = updateCurrentDestination,
                goToGroupGameSetup = goToGroupGameSetup,
                logout = logout,
                currentTheme = ThemeState.LIGHT,
                switchTheme = switchTheme,
                setUiReady = { },
                viewModel = viewModel
            )
        }
    }

    private fun setUpViewModel() {
        viewModel = HomeViewModel(
            thisUserUseCases,
            FakeDeleteChatsUseCase(),
            FakeGetChatsUseCase(chats)
        )
    }

    @get:Rule
    var testRule = createComposeRule()

    @Before
    fun setUp() {
        goToOnBoarding = mock()
        goToProfile = mock()
        goToDeveloper = mock()
        onChatClick = mock()
        updateCurrentDestination = mock()
        goToGroupGameSetup = mock()
        logout = mock()
        switchTheme = mock()
        thisUserUseCases = FakeThisUseUseCases(thisUser)

        setUpViewModel()
    }

    @Test
    fun homeScreenTest_userNameShown() {
        setContentOnComposeTestRule()
        testRule.onNodeWithText(thisUser.username).assertIsDisplayed()
    }

    @Test
    fun homeScreenTest_clickUserAvi() {
        setContentOnComposeTestRule()
        testRule.onNodeWithContentDescription(THIS_USER_AVI).performClick()
        verify(goToProfile).invoke()
    }

    @Test
    fun homeScreenTest_clickMenuButton_showMenu() {
        setContentOnComposeTestRule()
        testRule.onNodeWithContentDescription(HOME_MENU_BUTTON).performClick()
        testRule.onNodeWithContentDescription(HOME_MENU).assertIsDisplayed()
    }

    @Test
    fun homeScreenTest_clickGroupGameMenuItem() {
        setContentOnComposeTestRule()
        testRule.onNodeWithContentDescription(HOME_MENU_BUTTON).performClick()
        testRule.onNodeWithText("New Group Game").performClick()
        verify(goToGroupGameSetup).invoke()
    }

    @Test
    fun homeScreenTest_clickDeveloperMenuItem() {
        setContentOnComposeTestRule()
        testRule.onNodeWithContentDescription(HOME_MENU_BUTTON).performClick()
        testRule.onNodeWithText("Developer").performClick()
        verify(goToDeveloper).invoke()
    }

    @Test
    fun homeScreenTest_clickLogout() {
        setContentOnComposeTestRule()
        testRule.onNodeWithContentDescription(HOME_MENU_BUTTON).performClick()
        testRule.onNodeWithText("Log out").performClick()
        verify(logout).invoke()
    }

    @Test
    fun homeScreenTest_clickChat() {
        setContentOnComposeTestRule()
        testRule
            .onNodeWithContentDescription("$HOME_CHAT_ITEM ${chats[0].chatName}")
            .performClick()
        verify(onChatClick).invoke(chats[0].chatId)
    }

    @Test
    fun homeScreenTest_userIsNull_goesToOnBoarding() {
        thisUserUseCases = FakeThisUseUseCases(null)
        setUpViewModel()
        setContentOnComposeTestRule()
        verify(goToOnBoarding).invoke()
    }

    @Test
    fun homeScreenTest_clearEmptyCalled() {
        viewModel = spy(viewModel)
        setContentOnComposeTestRule()
        verify(viewModel).clearEmptyChats()
    }

    @Test
    fun homeScreenTest_refreshThisUserCalled() {
        viewModel = spy(viewModel)
        setContentOnComposeTestRule()
        verify(viewModel).refreshThisUser()
    }
}