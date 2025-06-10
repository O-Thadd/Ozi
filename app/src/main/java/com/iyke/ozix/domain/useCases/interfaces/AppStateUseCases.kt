package com.iyke.ozix.domain.useCases.interfaces

import com.iyke.ozix.domain.model.AppState
import kotlinx.coroutines.flow.Flow

interface AppStateUseCases {
    suspend fun incrementAppStartsCount()
    fun getFlow(): Flow<AppState>

    suspend fun get(): AppState

    suspend fun update(appState: AppState)
}