package com.iyke.ozix.domain.repos

import com.iyke.ozix.data.dataSources.model.OpError
import com.iyke.ozix.data.repos.SignUpError
import com.iyke.ozix.domain.model.OperationOutcome
import com.iyke.ozix.domain.model.OperationOutcomeX
import com.iyke.ozix.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

interface ThisUserRepo {

    suspend fun get(): User? = getFlow().first()

    suspend fun refresh(): OperationOutcomeX<Nothing, OpError>

    fun getFlow(): Flow<User?>

    suspend fun register(
        username: String,
        password: String,
        aviFg: Int,
        aviBg: Int,
    ): OperationOutcomeX<Nothing, SignUpError>

    suspend fun login(
        username: String,
        password: String,
        fcmToken: String
    ): OperationOutcomeX<Nothing, OpError>

    suspend fun updateLocal(user: User?)

    suspend fun updateRemote(user: User): OperationOutcome<Nothing, Nothing>

}