package com.othadd.ozi.data.repos

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.othadd.ozi.common.STATUS_FAILURE
import com.othadd.ozi.data.dataSources.localStore.OziDataStore
import com.othadd.ozi.data.dataSources.model.RemoteUsersRequestType
import com.othadd.ozi.data.dataSources.remote.OziRemoteService
import com.othadd.ozi.domain.model.ServerResponseStatus
import com.othadd.ozi.domain.repos.ThisUserRepo
import com.othadd.ozi.domain.model.OperationOutcome
import com.othadd.ozi.domain.model.User
import com.othadd.ozi.domain.model.gaming.UserGameState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import org.json.JSONObject
import javax.inject.Inject

class ThisUserRepoImpl @Inject constructor(
    private val dataStore: OziDataStore,
    private val remoteService: OziRemoteService
) : ThisUserRepo {

    override suspend fun refresh(): OperationOutcome<Nothing, Nothing> {
        val thisUser = dataStore.getThisUserFlow().first()
        thisUser ?: return OperationOutcome.Successful()
        return try {
            val latestUser =
                remoteService.getUsers(
                    requestType = RemoteUsersRequestType.SPECIFIC.string,
                    userId = thisUser.userId
                )[0]
            dataStore.updateThisUser(latestUser.copy(token = thisUser.token))
            OperationOutcome.Successful()
        } catch (e: Exception) {
            OperationOutcome.Failed()
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
        fcmToken: String
    ): OperationOutcome<Nothing, ServerResponseStatus?> {
        val response: JsonObject
        try {
            response = remoteService.registerUser(username, password, aviFg, aviBg, fcmToken)
        } catch (e: Exception) {
            Log.e("utilRepo. registerUser", e.message ?: e.toString())
            return OperationOutcome.Failed()
        }

        return if (response.get("status").asString == STATUS_FAILURE) {
            OperationOutcome.Failed(ServerResponseStatus.FAILURE)
        } else {
            val userId = response.get("userId").asString
            val token = response.get("token").asString

            val user = User(
                userId, username, aviFg, aviBg, true, false, token, UserGameState.AVAILABLE.string
            )
            dataStore.updateThisUser(user)

            OperationOutcome.Successful()
        }
    }

    override suspend fun login(
        username: String,
        password: String,
        fcmToken: String
    ): OperationOutcome<Nothing, ServerResponseStatus?> {
        val response: JsonObject
        try {
            response = remoteService.login(
                username,
                password,
                fcmToken
            )
        } catch (e: Exception) {
            return OperationOutcome.Failed()
        }

        return if (response.get("status").asString == STATUS_FAILURE) {
            OperationOutcome.Failed(ServerResponseStatus.FAILURE)
        } else {
            val userString = response.get("user").asString
            val user = Gson().fromJson(userString, User::class.java)
            dataStore.updateThisUser(user)
            OperationOutcome.Successful()
        }
    }

    override suspend fun updateLocal(user: User?) {
        dataStore.updateThisUser(user)
    }

    override suspend fun updateRemote(user: User): OperationOutcome<Nothing, Nothing> {
        return try {
            dataStore.getThisUserFlow().first()?.token?.let {
                val updateJson = JSONObject(Gson().toJson(user))
                updateJson.put("requesterId", dataStore.getThisUserFlow().first()!!.userId)
                remoteService.postToUser(updateJson.toString(), it)
            }
            OperationOutcome.Successful()
        } catch (e: Exception) {
            OperationOutcome.Failed()
        }
    }

}