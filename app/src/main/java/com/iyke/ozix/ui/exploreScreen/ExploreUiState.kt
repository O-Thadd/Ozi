package com.iyke.ozix.ui.exploreScreen

import com.iyke.ozix.domain.model.OziData
import com.iyke.ozix.domain.model.User

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
