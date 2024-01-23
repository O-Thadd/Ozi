package com.othadd.ozi.domain.useCases.interfaces.user

import com.othadd.ozi.domain.model.OperationOutcome
import com.othadd.ozi.domain.model.ServerResponseStatus
import kotlinx.coroutines.CoroutineScope

interface RegisterUserUseCase {
    suspend operator fun invoke(
        username: String,
        password: String,
        aviFg: Int,
        aviBg: Int,
        scope: CoroutineScope,
        outcomeHandler: (OperationOutcome<Nothing, ServerResponseStatus?>) -> Unit
    )
}