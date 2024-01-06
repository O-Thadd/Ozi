package com.othadd.ozi.domain.useCases.user

import com.google.firebase.messaging.FirebaseMessaging
import com.othadd.ozi.domain.model.ServerResponseStatus
import com.othadd.ozi.domain.repos.ThisUserRepo
import com.othadd.ozi.domain.model.OperationOutcome
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginUserUseCase @Inject constructor(private val thisUserRepo: ThisUserRepo) {

    suspend operator fun invoke(
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