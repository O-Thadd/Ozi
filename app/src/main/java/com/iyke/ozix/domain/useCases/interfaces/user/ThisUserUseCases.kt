package com.iyke.ozix.domain.useCases.interfaces.user

import com.iyke.ozix.data.dataSources.model.OpError
import com.iyke.ozix.domain.model.OperationOutcome
import com.iyke.ozix.domain.model.OperationOutcomeX
import com.iyke.ozix.domain.model.User
import kotlinx.coroutines.flow.Flow

interface ThisUserUseCases {
    fun get(): Flow<User?>

    suspend fun refresh(): OperationOutcomeX<Nothing, OpError>

    suspend fun updateLocal(user: User?)
    suspend fun updateRemote(user: User): OperationOutcome<Nothing, Nothing>
}