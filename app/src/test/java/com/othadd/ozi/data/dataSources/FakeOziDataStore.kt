package com.othadd.ozi.data.dataSources

import com.google.gson.Gson
import com.othadd.ozi.data.dataSources.localStore.OziDataStore
import com.othadd.ozi.domain.model.AppState
import com.othadd.ozi.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeOziDataStore: OziDataStore {
    private var user: User? = null
    private var prevSearches = ""
    private var appState = Gson().toJson(AppState.DEFAULT)

    private val userFlow = MutableStateFlow(user)
    private val prevSearchesFlow = MutableStateFlow(prevSearches)
    private val appStateFlow = MutableStateFlow(appState)

    override suspend fun updateThisUser(user: User?) {
        this.user = user
        userFlow.value = user
    }

    override fun getThisUserFlow(): Flow<User?> {
        return userFlow
    }

    override suspend fun updatePrevSearches(update: String) {
        prevSearches = update
        prevSearchesFlow.value = prevSearches
    }

    override fun getPrevSearches(): Flow<String> {
        return prevSearchesFlow
    }

    override suspend fun updateAppState(update: String) {
        appState = update
        appStateFlow.value = appState
    }

    override fun getAppState(): Flow<String> {
        return appStateFlow
    }
}