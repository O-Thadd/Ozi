package com.iyke.ozix.domain.repos

import com.iyke.ozix.domain.model.AppState
import kotlinx.coroutines.flow.Flow

interface AppStateRepo {

    suspend fun incrementAppStartCount()

    fun get(): Flow<AppState>

    suspend fun update(appState: AppState)
}