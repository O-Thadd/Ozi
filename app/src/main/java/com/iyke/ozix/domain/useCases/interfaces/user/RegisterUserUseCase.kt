package com.iyke.ozix.domain.useCases.interfaces.user

import com.iyke.ozix.data.repos.SignUpError
import com.iyke.ozix.domain.model.OperationOutcomeX
import kotlinx.coroutines.CoroutineScope

interface RegisterUserUseCase {
    suspend operator fun invoke(
        username: String,
        password: String,
        aviFg: Int,
        aviBg: Int,
        scope: CoroutineScope,
        outcomeHandler: (OperationOutcomeX<Nothing, SignUpError>) -> Unit
    )
}