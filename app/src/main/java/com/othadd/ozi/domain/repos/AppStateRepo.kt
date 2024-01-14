package com.othadd.ozi.domain.repos

import com.othadd.ozi.domain.model.AppState
import kotlinx.coroutines.flow.Flow

interface AppStateRepo {

    suspend fun incrementAppStartCount()

    fun get(): Flow<AppState>

    suspend fun update(appState: AppState)
}