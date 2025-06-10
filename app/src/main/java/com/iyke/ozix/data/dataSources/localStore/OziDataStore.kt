package com.iyke.ozix.data.dataSources.localStore

import com.iyke.ozix.domain.model.User
import kotlinx.coroutines.flow.Flow

interface OziDataStore {
    suspend fun updateThisUser(user: User?)
    fun getThisUserFlow(): Flow<User?>
    suspend fun updatePrevSearches(update: String)
    fun getPrevSearches(): Flow<String>

    suspend fun updateAppState(update: String)
    fun getAppState(): Flow<String>
}