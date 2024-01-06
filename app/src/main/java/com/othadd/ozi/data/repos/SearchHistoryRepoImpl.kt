package com.othadd.ozi.data.repos

import com.othadd.ozi.common.MAX_NUMBER_OF_PREV_SEARCHES
import com.othadd.ozi.data.dataSources.localStore.OziDataStore
import com.othadd.ozi.domain.repos.SearchHistoryRepo
import com.othadd.ozi.common.stringListToString
import com.othadd.ozi.common.stringToStringList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SearchHistoryRepoImpl @Inject constructor(private val dataStore: OziDataStore) :
    SearchHistoryRepo {
    override suspend fun addToSearchHistory(searchTerm: String) {
        val searches = stringToStringList(dataStore.getPrevSearches().first()).toMutableList()

        if (searches.contains(searchTerm)){
            return
        }

        if (searches.size == MAX_NUMBER_OF_PREV_SEARCHES) {
            searches.removeAt(MAX_NUMBER_OF_PREV_SEARCHES - 1)
        }

        searches.add(0, searchTerm)
        dataStore.updatePrevSearches(stringListToString(searches))
    }

    override suspend fun getSearchHistory(): Flow<List<String>> {
        return dataStore.getPrevSearches().map {
            stringToStringList(it)
        }
    }

    override suspend fun removeFromSearchHistory(searchTerm: String) {
        val searches = stringToStringList(dataStore.getPrevSearches().first()).toMutableList()
        searches.remove(searchTerm)
        dataStore.updatePrevSearches(stringListToString(searches))
    }

    override suspend fun clearSearchHistory() {
        dataStore.updatePrevSearches("")
    }
}