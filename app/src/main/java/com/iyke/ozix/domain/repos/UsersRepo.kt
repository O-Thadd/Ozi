package com.iyke.ozix.domain.repos

import com.iyke.ozix.data.dataSources.model.OpError
import com.iyke.ozix.domain.model.DataSourcePreference
import com.iyke.ozix.domain.model.OperationOutcome
import com.iyke.ozix.domain.model.OperationOutcomeX
import com.iyke.ozix.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UsersRepo {

    suspend fun getUser(userId: String, sourcePreference: DataSourcePreference): OperationOutcome<User?, Nothing>

    suspend fun getUserFlow(userId: String): Flow<User>

    /**
     * Performs an update instead, if a user with the same [User.userId] field already exists
     */
    suspend fun addUser(user: User)

    suspend fun syncUser(userId: String)

    suspend fun exploreUsers(): OperationOutcomeX<List<User>, OpError>

    suspend fun searchUsers(searchTerm: String): OperationOutcomeX<List<User>, OpError>

    suspend fun deleteAll()

}