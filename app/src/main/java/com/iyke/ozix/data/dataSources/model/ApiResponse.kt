package com.iyke.ozix.data.dataSources.model

import com.google.gson.Gson
import com.iyke.ozix.domain.model.OperationOutcomeX
import retrofit2.Response

typealias OziResponse<T> = Response<ApiResponse<T>>

data class ApiResponse<T>(
    val successful: Boolean,
    val message: String?,
    val data: T?,
)

data class ErrorApiResponse(
    val successful: Boolean,
    val message: String?,
)

data class CreateUserResponse(
    val userId: String
)

data class RegisterUserPayload(
    val username: String,
    val password: String,
    val aviFg: Int,
    val aviBg: Int,
    val fcmToken: String = ""
)

data class LoginPayload(
    val username: String,
    val password: String,
    val fcmToken: String,
)

val gson = Gson()

class ApiCall<T, U, V: OpError>(
    val action: suspend () -> OziResponse<T>,
    val onSuccess: suspend (T) -> OperationOutcomeX.Successful<U>,
    val onFailure: (String) -> OperationOutcomeX.Failed<V>
) {
    suspend fun call(): OperationOutcomeX<U, V> {
        return try {
            val response = action()
            if (response.isSuccessful) {
                onSuccess(response.body()!!.data!!)
            } else {
                val errorBody = response.errorBody()!!.string()
                val errorResponse = gson.fromJson(errorBody, ErrorApiResponse::class.java)
                onFailure(errorResponse.message!!)
            }
        } catch (e: Exception) {
            onFailure(e.message ?: e.toString())
        }
    }

    companion object {
        suspend operator fun <T, U, V: OpError> invoke(block: ApiCallRunnerBuilderScope<T, U, V>.() -> Unit) =
            ApiCallRunnerBuilderScope<T, U, V>().apply(block).run()
    }
}

class ApiCallRunnerBuilderScope<T, U, V: OpError> {
    private lateinit var actionX: suspend () -> OziResponse<T>
    private lateinit var onSuccess: suspend (T) -> OperationOutcomeX.Successful<U>
    private lateinit var onFailure: (String) -> OperationOutcomeX.Failed<V>

    fun action(action: suspend () -> OziResponse<T>) { actionX = action }

    fun success(onSuccess: suspend (T) -> OperationOutcomeX.Successful<U>) { this.onSuccess = onSuccess }

    fun failure(onFailure: (String) -> OperationOutcomeX.Failed<V>) { this.onFailure = onFailure }

    fun build() = ApiCall(actionX, onSuccess, onFailure)

    suspend fun run() = build().call()
}

interface OpError