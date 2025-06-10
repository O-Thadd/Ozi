package com.iyke.ozix.domain.useCases.defaultImplementations.user

import com.iyke.ozix.data.dataSources.model.OpError
import com.iyke.ozix.domain.repos.ThisUserRepo
import com.iyke.ozix.domain.model.OperationOutcomeX
import com.iyke.ozix.domain.useCases.interfaces.user.LoginUserUseCase
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class LoginUserUseCaseImpl @Inject constructor(private val thisUserRepo: ThisUserRepo) :
    LoginUserUseCase {

    override suspend operator fun invoke(
        username: String,
        password: String,
        scope: CoroutineScope,
        outcomeHandler: (OperationOutcomeX<Nothing, OpError>) -> Unit
    ) {
        val outcome = thisUserRepo.login(username, password, "")
        outcomeHandler(outcome)
    }
}