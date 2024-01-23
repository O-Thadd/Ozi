package com.othadd.ozi.domain.useCases.interfaces

import com.othadd.ozi.domain.model.AppState
import kotlinx.coroutines.flow.Flow

interface AppStateUseCases {
    suspend fun incrementAppStartsCount()
    fun getFlow(): Flow<AppState>

    suspend fun get(): AppState

    suspend fun update(appState: AppState)
}