package com.othadd.ozi.domain.useCases

import com.othadd.ozi.domain.model.AppState
import com.othadd.ozi.domain.repos.AppStateRepo
import com.othadd.ozi.domain.repos.ThisUserRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class AppStateUseCases @Inject constructor(
    private val appStateRepo: AppStateRepo,
    private val thisUserRepo: ThisUserRepo
) {

    suspend fun incrementAppStartsCount() {
        appStateRepo.incrementAppStartCount()
    }

    fun getFlow(): Flow<AppState> {
        val thisUserFlow = thisUserRepo.getFlow()
        val appStateFlow = appStateRepo.get()
        return combine(thisUserFlow, appStateFlow){ thisUser, appState ->
            appState.copy(signedIn = thisUser != null)
        }
    }

    suspend fun get(): AppState {
        return appStateRepo.get().first()
    }

    suspend fun update(appState: AppState) {
        appStateRepo.update(appState)
    }
}