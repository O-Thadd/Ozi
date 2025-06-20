package com.iyke.ozix.domain.useCases.user

import com.iyke.ozix.data.dataSources.model.OpError
import com.iyke.ozix.domain.model.OperationOutcome
import com.iyke.ozix.domain.model.OperationOutcomeX
import com.iyke.ozix.domain.model.User
import com.iyke.ozix.domain.useCases.interfaces.user.ThisUserUseCases
import com.iyke.ozix.testUser1
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeThisUseUseCases(private var userToReturn: User?): ThisUserUseCases {
    private var networkError = false

    override fun get(): Flow<User?> {
        return flowOf(userToReturn)
    }

    override suspend fun refresh(): OperationOutcomeX<Nothing, OpError> {
        return if (networkError){
            OperationOutcomeX.Failed()
        }
        else {
            OperationOutcomeX.Successful()
        }
    }

    override suspend fun updateLocal(user: User?) {  }

    override suspend fun updateRemote(user: User): OperationOutcome<Nothing, Nothing> {
        return if (networkError){
            OperationOutcome.Failed()
        }
        else {
            OperationOutcome.Successful()
        }
    }

    fun setNetworkError(newValue: Boolean){
        networkError = newValue
    }

    fun setUserToReturn(user: User){
        userToReturn = user
    }
}