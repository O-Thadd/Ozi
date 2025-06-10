package com.iyke.ozix.e2e

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.espresso.Espresso
import androidx.test.filters.LargeTest
import com.iyke.ozix.common.HOME_MENU
import com.iyke.ozix.common.HOME_MENU_BUTTON
import com.iyke.ozix.common.HOME_SCREEN
import com.iyke.ozix.data.dataSources.remote.FakeOziRemoteService
import com.iyke.ozix.data.dataSources.remote.OziRemoteService
import com.iyke.ozix.di.RemoteServiceModule
import com.iyke.ozix.di.SplashSetupRunnerModule
import com.iyke.ozix.ui.FakeSplashSetupRunner
import com.iyke.ozix.ui.MainActivity
import com.iyke.ozix.ui.model.SplashScreenSetupRunner
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.After
import org.junit.Rule
import org.junit.Test

@UninstallModules(RemoteServiceModule::class, SplashSetupRunnerModule::class)
@HiltAndroidTest
@LargeTest
class LoginFlow {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var testRule = createAndroidComposeRule<MainActivity>()

    @BindValue
    @JvmField
    val remoteService: OziRemoteService = FakeOziRemoteService()

    @BindValue
    @JvmField
    val splashSetupRunner: SplashScreenSetupRunner = FakeSplashSetupRunner()

    @After
    fun tearDown(){
        //logOut
        testRule.apply {
            testRule.onNodeWithContentDescription(HOME_MENU_BUTTON).performClick()
            testRule.onNodeWithContentDescription(HOME_MENU).assertIsDisplayed()
            testRule.onNodeWithText("Log out").performClick()
            testRule.onNodeWithText("Yes").performClick()
        }
    }


    @OptIn(ExperimentalTestApi::class)
    @Test
    fun login(){
        testRule.apply {
            onNodeWithText("Login").performClick()
            onNodeWithText("Username").performTextInput("testname")
            onNodeWithText("Password").performTextInput("testpassword")
            Espresso.closeSoftKeyboard()
            onNodeWithText("Login").assertIsEnabled()
            onNodeWithText("Login").performClick()
            waitUntilNodeCount(hasContentDescription(HOME_SCREEN), 1, 10_000)
            onNodeWithContentDescription(HOME_SCREEN).assertExists()
        }
    }

}