package com.iyke.ozix.domain.useCases.interfaces.user

import com.iyke.ozix.data.dataSources.model.OpError
import com.iyke.ozix.domain.model.DataSourcePreference
import com.iyke.ozix.domain.model.OperationOutcomeX
import com.iyke.ozix.domain.model.User
import kotlinx.coroutines.flow.Flow

interface GetUsersUseCases {
    suspend fun explore(): OperationOutcomeX<List<User>, OpError>

    suspend fun search(searchTerm: String): OperationOutcomeX<List<User>, OpError>

    suspend fun getUserFlow(userId: String): Flow<User>

    suspend fun getUser(
        userId: String,
        dataSourcePreference: DataSourcePreference = DataSourcePreference.LOCAL
    ): User?

    suspend fun syncUser(userId: String)
}