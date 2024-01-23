package com.othadd.ozi.domain.useCases.implementations

import com.othadd.ozi.domain.model.AppState
import com.othadd.ozi.domain.repos.AppStateRepo
import com.othadd.ozi.domain.repos.ThisUserRepo
import com.othadd.ozi.domain.useCases.interfaces.AppStateUseCases
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class AppStateUseCasesImpl @Inject constructor(
    private val appStateRepo: AppStateRepo,
    private val thisUserRepo: ThisUserRepo
) : AppStateUseCases {

    override suspend fun incrementAppStartsCount() {
        appStateRepo.incrementAppStartCount()
    }

    override fun getFlow(): Flow<AppState> {
        val thisUserFlow = thisUserRepo.getFlow()
        val appStateFlow = appStateRepo.get()
        return combine(thisUserFlow, appStateFlow){ thisUser, appState ->
            appState.copy(signedIn = thisUser != null)
        }
    }

    override suspend fun get(): AppState {
        return appStateRepo.get().first()
    }

    override suspend fun update(appState: AppState) {
        appStateRepo.update(appState)
    }
}