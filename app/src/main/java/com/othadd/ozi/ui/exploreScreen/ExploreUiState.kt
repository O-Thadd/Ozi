package com.othadd.ozi.ui.exploreScreen

import com.othadd.ozi.domain.model.OziData
import com.othadd.ozi.domain.model.User

data class ExploreUiState(
    val exploredUsers: OziData<List<User>>,
    val searchedUsers: OziData<List<User>>,
    val prevSearches: List<String>
){
    companion object {
        private val emptyListData = OziData.Available(emptyList<User>())
        val DEFAULT = ExploreUiState(emptyListData, emptyListData, emptyList())
    }
}
