package com.othadd.ozi.domain.repos

import kotlinx.coroutines.flow.Flow

interface SearchHistoryRepo {

    suspend fun addToSearchHistory(searchTerm: String)

    suspend fun getSearchHistory(): Flow<List<String>>

    suspend fun removeFromSearchHistory(searchTerm: String)

    suspend fun clearSearchHistory()
}