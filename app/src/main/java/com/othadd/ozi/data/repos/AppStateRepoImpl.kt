package com.othadd.ozi.data.repos

import com.google.gson.Gson
import com.othadd.ozi.data.dataSources.localStore.DefaultOziDataStore
import com.othadd.ozi.data.dataSources.localStore.OziDataStore
import com.othadd.ozi.domain.model.AppState
import com.othadd.ozi.domain.repos.AppStateRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AppStateRepoImpl @Inject constructor(private val dataStore: OziDataStore) : AppStateRepo {
    private val gson = Gson()

    override suspend fun incrementAppStartCount() {
        val initialStateString = dataStore.getAppState().first()
        val initialState = gson.fromJson(initialStateString, AppState::class.java)
        val updatedState =
            initialState.copy(appStartsCount = initialState.appStartsCount + 1, inForeground = true)
        dataStore.updateAppState(gson.toJson(updatedState))
    }

    override fun get(): Flow<AppState> {
        return dataStore.getAppState().map {
            gson.fromJson(it, AppState::class.java)
        }
    }

    override suspend fun update(appState: AppState) {
        dataStore.updateAppState(gson.toJson(appState))
    }
}