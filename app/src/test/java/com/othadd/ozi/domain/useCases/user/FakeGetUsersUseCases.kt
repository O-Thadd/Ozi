package com.othadd.ozi.domain.useCases.user

import com.othadd.ozi.domain.model.DataSourcePreference
import com.othadd.ozi.domain.model.OperationOutcome
import com.othadd.ozi.domain.model.User
import com.othadd.ozi.domain.useCases.interfaces.user.GetUsersUseCases
import com.othadd.ozi.testUser1
import com.othadd.ozi.testUser2
import com.othadd.ozi.testUser3
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeGetUsersUseCases: GetUsersUseCases {
    private var networkError = false
    private val users = listOf(testUser1, testUser2, testUser3)
    override suspend fun explore(): OperationOutcome<List<User>, Nothing> {
        if (networkError){
            return OperationOutcome.Failed()
        }

        return OperationOutcome.Successful(users)
    }

    override suspend fun search(searchTerm: String): OperationOutcome<List<User>, Nothing> {
        if (networkError){
            return OperationOutcome.Failed()
        }

        return OperationOutcome.Successful(users)
    }

    override suspend fun getUserFlow(userId: String): Flow<User> {
        return flowOf(testUser1)
    }

    override suspend fun getUser(
        userId: String,
        dataSourcePreference: DataSourcePreference
    ): User? {
        return testUser1
    }

    override suspend fun syncUser(userId: String) {  }

    fun setNetworkError(newValue: Boolean){
        networkError = newValue
    }
}