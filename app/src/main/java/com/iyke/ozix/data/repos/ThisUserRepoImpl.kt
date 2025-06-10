package com.iyke.ozix.data.repos

import android.util.Log
import com.iyke.ozix.data.dataSources.localStore.OziDataStore
import com.iyke.ozix.data.dataSources.model.ApiCall
import com.iyke.ozix.data.dataSources.model.LoginPayload
import com.iyke.ozix.data.dataSources.model.OpError
import com.iyke.ozix.data.dataSources.model.RegisterUserPayload
import com.iyke.ozix.data.dataSources.remote.OziRemoteService
import com.iyke.ozix.domain.model.FromApiUser
import com.iyke.ozix.domain.repos.ThisUserRepo
import com.iyke.ozix.domain.model.OperationOutcome
import com.iyke.ozix.domain.model.OperationOutcomeX
import com.iyke.ozix.domain.model.User
import com.iyke.ozix.domain.model.gaming.UserGameState
import com.iyke.ozix.domain.model.toApiUser
import com.iyke.ozix.domain.model.toUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class ThisUserRepoImpl @Inject constructor(
    private val dataStore: OziDataStore,
    private val remoteService: OziRemoteService
) : ThisUserRepo {

    override suspend fun refresh(): OperationOutcomeX<Nothing, OpError> {
        val thisUser = dataStore.getThisUserFlow().first()
        thisUser ?: return OperationOutcomeX.Successful()

        val opOutcome = ApiCall<FromApiUser, User, OpError> {
            action { remoteService.getUser(userId = thisUser.userId) }
            success { OperationOutcomeX.Successful(it.toUser()) }
            failure { OperationOutcomeX.Failed() }
        }

        return if (opOutcome is OperationOutcomeX.Successful) {
            dataStore.updateThisUser(opOutcome.data!!)
            OperationOutcomeX.Successful()
        } else {
            OperationOutcomeX.Failed()
        }
    }

    override fun getFlow(): Flow<User?> {
        return dataStore.getThisUserFlow()
    }

    override suspend fun register(
        username: String,
        password: String,
        aviFg: Int,
        aviBg: Int,
    ): OperationOutcomeX<Nothing, SignUpError> {

        return ApiCall(
            action = {
                val payload = RegisterUserPayload(username, password, aviFg, aviBg)
                remoteService.registerUser(payload)
            },
            onSuccess = {
                val user = User(
                    it.userId, username, aviFg, aviBg, true, false, it.userId, UserGameState.AVAILABLE.string
                )
                dataStore.updateThisUser(user)

                OperationOutcomeX.Successful<Nothing>()
            },
            onFailure = {
                val signUpError = SignUpError.getByApiMessage(it)
                OperationOutcomeX.Failed(signUpError)
            }
        ).call()
    }

    override suspend fun login(
        username: String,
        password: String,
        fcmToken: String
    ): OperationOutcomeX<Nothing, OpError> {

        return ApiCall<FromApiUser, Nothing, LoginError> {
            action {
                val payload = LoginPayload(username, password, fcmToken)
                remoteService.login(payload)
            }

            success {
                dataStore.updateThisUser(it.toUser())
                OperationOutcomeX.Successful()
            }

            failure {
                OperationOutcomeX.Failed(LoginError.matchApiMessage(it))
            }
        }
    }

    override suspend fun updateLocal(user: User?) {
        dataStore.updateThisUser(user)
    }

    override suspend fun updateRemote(user: User): OperationOutcome<Nothing, Nothing> {
        return try {
            remoteService.updateUser(user.userId, user.toApiUser())
            OperationOutcome.Successful()
        } catch (e: Exception) {
            Log.e("zzz", "$e")
            OperationOutcome.Failed()
        }
    }
}

enum class SignUpError(val apiMessage: String): OpError {
    USER_NAME_NOT_AVAILABLE("is not available"),
    ;

    companion object {
        fun getByApiMessage(apiMessage: String) =
            values().find { it.apiMessage.contains(apiMessage) || apiMessage.contains(it.apiMessage) }

    }
}

object LoginError: OpError {
    private const val LOGIN_ERROR_API_MESSAGE: String = "incorrect credentials"
    fun matchApiMessage(apiMessage: String) =
        if (LOGIN_ERROR_API_MESSAGE == apiMessage) { this } else { null }
}