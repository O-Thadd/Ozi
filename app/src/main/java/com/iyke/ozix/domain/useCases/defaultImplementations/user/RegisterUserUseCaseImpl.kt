package com.iyke.ozix.domain.useCases.defaultImplementations.user

import com.iyke.ozix.data.repos.SignUpError
import com.iyke.ozix.domain.repos.ThisUserRepo
import com.iyke.ozix.domain.model.OperationOutcomeX
import com.iyke.ozix.domain.useCases.interfaces.user.RegisterUserUseCase
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class RegisterUserUseCaseImpl @Inject constructor(private val thisUserRepo: ThisUserRepo) :
    RegisterUserUseCase {

    override suspend operator fun invoke(
        username: String,
        password: String,
        aviFg: Int,
        aviBg: Int,
        scope: CoroutineScope,
        outcomeHandler: (OperationOutcomeX<Nothing, SignUpError>) -> Unit
    ) {

        val outcome = thisUserRepo.register(username, password, aviFg, aviBg)
        outcomeHandler(outcome)

    }
}