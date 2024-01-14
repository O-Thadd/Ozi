package com.othadd.ozi.data.repos

import com.othadd.ozi.data.dataSources.FakeOziDataStore
import com.othadd.ozi.domain.model.AppState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class AppStateRepoImplTest {

    private lateinit var appStateRepoImpl: AppStateRepoImpl

    @Before
    fun setUp() {
        appStateRepoImpl = AppStateRepoImpl(FakeOziDataStore())
    }

    @Test
    fun appStateRepoImpl_incrementAppStartCount_incremented() = runTest {
        appStateRepoImpl.incrementAppStartCount()
        val retrievedAppState = appStateRepoImpl.get().first()
        assertEquals(AppState.DEFAULT.appStartsCount + 1, retrievedAppState.appStartsCount)
    }

    @Test
    fun appStateRepoImpl_get_returnsDefaultAppState() = runTest {
        val retrievedAppState = appStateRepoImpl.get().first()
        assertEquals(AppState.DEFAULT, retrievedAppState)
    }

    @Test
    fun appStateRepoImpl_update_updated() = runTest {
        val newAppState = AppState.DEFAULT.copy(gameModeratorId = "tesUpdatedGameModerator")
        appStateRepoImpl.update(newAppState)
        val retrievedAppState = appStateRepoImpl.get().first()
        assertEquals(newAppState, retrievedAppState)
    }

}