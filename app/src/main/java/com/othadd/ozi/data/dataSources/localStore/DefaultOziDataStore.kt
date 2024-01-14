package com.othadd.ozi.data.dataSources.localStore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.gson.Gson
import com.othadd.ozi.common.NO_VALUE_SET
import com.othadd.ozi.domain.model.AppState
import com.othadd.ozi.domain.model.User
import com.othadd.ozi.ui.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultOziDataStore @Inject constructor(private val context: Context) : OziDataStore {

    private val thisUserKey = stringPreferencesKey("thisUserKey")
    private val previousSearchesKey = stringPreferencesKey("prevSearchesKey")
    private val appStateKey = stringPreferencesKey("appStateKey")

    override suspend fun updateThisUser(user: User?){
        user ?: suspend {
            context.dataStore.edit {
                it[thisUserKey] = NO_VALUE_SET
            }
        }

        val userString = Gson().toJson(user)
        context.dataStore.edit {
            it[thisUserKey] = userString
        }
    }

    override fun getThisUserFlow(): Flow<User?>{
        return context.dataStore.data.map {
            val userString = it[thisUserKey]
            userString ?: return@map null
            if (userString == NO_VALUE_SET) return@map null
            Gson().fromJson(userString, User::class.java)
        }
    }

    override suspend fun updatePrevSearches(update: String) {
        context.dataStore.edit {
            it[previousSearchesKey] = update
        }
    }

    override fun getPrevSearches(): Flow<String> {
        return context.dataStore.data.map {
            it[previousSearchesKey] ?: ""
        }
    }

    override suspend fun updateAppState(update: String) {
        context.dataStore.edit {
            it[appStateKey] = update
        }
    }

    override fun getAppState(): Flow<String> {
        return context.dataStore.data.map {
            it[appStateKey] ?: Gson().toJson(AppState.DEFAULT)
        }
    }
}