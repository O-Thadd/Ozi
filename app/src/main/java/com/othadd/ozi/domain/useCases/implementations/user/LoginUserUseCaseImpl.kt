package com.othadd.ozi.domain.useCases.implementations.user

import com.google.firebase.messaging.FirebaseMessaging
import com.othadd.ozi.domain.model.ServerResponseStatus
import com.othadd.ozi.domain.repos.ThisUserRepo
import com.othadd.ozi.domain.model.OperationOutcome
import com.othadd.ozi.domain.useCases.interfaces.user.LoginUserUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginUserUseCaseImpl @Inject constructor(private val thisUserRepo: ThisUserRepo) :
    LoginUserUseCase {

    override suspend operator fun invoke(
        username: String,
        password: String,
        scope: CoroutineScope,
        outcomeHandler: (OperationOutcome<Nothing, ServerResponseStatus?>) -> Unit ){

        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            scope.launch {
                if (task.isSuccessful) {
                    val token = task.result
                    val outcome = thisUserRepo.login(username, password, token)
                    outcomeHandler(outcome)
                } else {
                    outcomeHandler(OperationOutcome.Failed())
                }
            }
        }
    }
}