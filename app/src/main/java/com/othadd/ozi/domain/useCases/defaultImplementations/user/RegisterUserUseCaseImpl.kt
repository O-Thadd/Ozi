package com.othadd.ozi.domain.useCases.defaultImplementations.user

import com.google.firebase.messaging.FirebaseMessaging
import com.othadd.ozi.domain.model.ServerResponseStatus
import com.othadd.ozi.domain.repos.ThisUserRepo
import com.othadd.ozi.domain.model.OperationOutcome
import com.othadd.ozi.domain.useCases.interfaces.user.RegisterUserUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class RegisterUserUseCaseImpl @Inject constructor(private val thisUserRepo: ThisUserRepo) :
    RegisterUserUseCase {

    override suspend operator fun invoke(
        username: String,
        password: String,
        aviFg: Int,
        aviBg: Int,
        scope: CoroutineScope,
        outcomeHandler: (OperationOutcome<Nothing, ServerResponseStatus?>) -> Unit
    ) {

        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            scope.launch {
                if (task.isSuccessful) {
                    val token = task.result
                    val outcome = thisUserRepo.register(username, password, aviFg, aviBg, token)
                    outcomeHandler(outcome)
                } else {
                    outcomeHandler(OperationOutcome.Failed())
                }
            }
        }
    }
}