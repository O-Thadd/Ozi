package com.iyke.ozix.domain.useCases.interfaces.user

import com.iyke.ozix.data.dataSources.model.OpError
import com.iyke.ozix.domain.model.OperationOutcomeX
import kotlinx.coroutines.CoroutineScope

interface LoginUserUseCase {
    suspend operator fun invoke(
        username: String,
        password: String,
        scope: CoroutineScope,
        outcomeHandler: (OperationOutcomeX<Nothing, OpError>) -> Unit
    )
}