package com.othadd.ozi.domain.useCases.interfaces.user

import com.othadd.ozi.domain.model.OperationOutcome
import com.othadd.ozi.domain.model.User
import kotlinx.coroutines.flow.Flow

interface ThisUserUseCases {
    fun get(): Flow<User?>

    suspend fun refresh(): OperationOutcome<Nothing, Nothing>

    suspend fun updateLocal(user: User?)
    suspend fun updateRemote(user: User): OperationOutcome<Nothing, Nothing>
}