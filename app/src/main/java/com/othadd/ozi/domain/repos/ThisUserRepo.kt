package com.othadd.ozi.domain.repos

import com.othadd.ozi.domain.model.ServerResponseStatus
import com.othadd.ozi.domain.model.OperationOutcome
import com.othadd.ozi.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

interface ThisUserRepo {

    suspend fun get(): User? = getFlow().first()

    suspend fun refresh(): OperationOutcome<Nothing, Nothing>

    fun getFlow(): Flow<User?>

    suspend fun register(
        username: String,
        password: String,
        aviFg: Int,
        aviBg: Int,
        fcmToken: String
    ): OperationOutcome<Nothing, ServerResponseStatus?>

    suspend fun login(
        username: String,
        password: String,
        fcmToken: String
    ): OperationOutcome<Nothing, ServerResponseStatus?>

    suspend fun updateLocal(user: User?)

    suspend fun updateRemote(user: User): OperationOutcome<Nothing, Nothing>

}