package com.othadd.ozi.domain.repos

import com.othadd.ozi.domain.model.DataSourcePreference
import com.othadd.ozi.domain.model.OperationOutcome
import com.othadd.ozi.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UsersRepo {

    suspend fun getUser(userId: String, sourcePreference: DataSourcePreference): OperationOutcome<User?, Nothing>

    suspend fun getUserFlow(userId: String): Flow<User>

    /**
     * Performs an update instead, if a user with the same [User.userId] field already exists
     */
    suspend fun addUser(user: User)

    suspend fun syncUser(userId: String)

    suspend fun exploreUsers(): OperationOutcome<List<User>, Nothing>

    suspend fun searchUsers(searchTerm: String): OperationOutcome<List<User>, Nothing>

    suspend fun deleteAll()

}