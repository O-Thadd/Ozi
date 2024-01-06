package com.othadd.ozi.domain.useCases

import com.othadd.ozi.domain.repos.SearchHistoryRepo
import javax.inject.Inject

class SearchHistoryUseCases @Inject constructor(private val searchHistoryRepo: SearchHistoryRepo) {

    suspend fun getPrevSearches() = searchHistoryRepo.getSearchHistory()

    suspend fun addToPrevSearch(searchTerm: String) =
        searchHistoryRepo.addToSearchHistory(searchTerm)

    suspend fun removePrevSearch(search: String) = searchHistoryRepo.removeFromSearchHistory(search)

    suspend fun clearHistory() = searchHistoryRepo.clearSearchHistory()
}