package com.othadd.ozi.domain.useCases.interfaces.user

import com.othadd.ozi.domain.model.DataSourcePreference
import com.othadd.ozi.domain.model.OperationOutcome
import com.othadd.ozi.domain.model.User
import kotlinx.coroutines.flow.Flow

interface GetUsersUseCases {
    suspend fun explore(): OperationOutcome<List<User>, Nothing>

    suspend fun search(searchTerm: String): OperationOutcome<List<User>, Nothing>

    suspend fun getUserFlow(userId: String): Flow<User>

    suspend fun getUser(
        userId: String,
        dataSourcePreference: DataSourcePreference = DataSourcePreference.LOCAL
    ): User?

    suspend fun syncUser(userId: String)
}