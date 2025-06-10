package com.iyke.ozix.data.dataSources.remote

import com.iyke.ozix.data.dataSources.model.CreateUserResponse
import com.iyke.ozix.data.dataSources.model.LoginPayload
import com.iyke.ozix.data.dataSources.model.OziResponse
import com.iyke.ozix.data.dataSources.model.RegisterUserPayload
import com.iyke.ozix.data.repos.SendMessageResponse
import com.iyke.ozix.domain.model.FromApiUser
import com.iyke.ozix.domain.model.message.FromApiMessage
import com.iyke.ozix.domain.model.Signal
import com.iyke.ozix.domain.model.ToApiUser
import com.iyke.ozix.domain.model.chat.ApiChat
import com.iyke.ozix.domain.model.message.ToApiMessage
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface OziRemoteService {

    @POST("users")
    suspend fun registerUser(
        @Body payload: RegisterUserPayload,
    ): OziResponse<CreateUserResponse>

    @PUT("users/{userId}")
    suspend fun updateUser(
        @Path("userId") userId: String,
        @Body user: ToApiUser,
    ): OziResponse<Unit>

    @POST("users/login")
    suspend fun login(
        @Body payload: LoginPayload,
    ): OziResponse<FromApiUser>

    @GET("users")
    suspend fun getAllUsers(): OziResponse<List<FromApiUser>>

    @GET("users")
    suspend fun searchUsers(
        @Query("q") username: String,
    ): OziResponse<List<FromApiUser>>

    @GET("users/{userId}")
    suspend fun getUser(
        @Path("userId") userId: String,
    ): OziResponse<FromApiUser>

    @POST("chats/messages")
    suspend fun sendMessage(
        @Body message: ToApiMessage,
    ): OziResponse<SendMessageResponse>

    @GET("chats/{chatId}/messages")
    suspend fun getMessages(
        @Path("chatId") chatId: String,
    ): OziResponse<List<FromApiMessage>>

    @GET("signals")
    suspend fun getSignals(@Header("x-token") token: String): List<Signal>

//    @GET("chats")
//    suspend fun getChats(
//        @Query("chatId") chatId: String? = null,
//        @Header("x-token") token: String
//    ): List<ApiChat>

    @GET("chats")
    suspend fun getUserChats(
    ): OziResponse<List<ApiChat>>

    @POST("gaming")
    suspend fun postToGaming(
        @Query("gaming_data") data: String,
        @Header("x-token") token: String
    ): String

    @GET("ping")
    suspend fun ping(): Boolean
}