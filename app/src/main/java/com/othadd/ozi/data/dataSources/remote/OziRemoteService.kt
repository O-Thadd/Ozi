package com.othadd.ozi.data.dataSources.remote

import com.google.gson.JsonObject
import com.othadd.ozi.common.BASE_URL
import com.othadd.ozi.domain.model.chat.ChatDto
import com.othadd.ozi.domain.model.message.MessageDto
import com.othadd.ozi.domain.model.Signal
import com.othadd.ozi.domain.model.User
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

private val interceptor = HttpLoggingInterceptor()
    .setLevel(HttpLoggingInterceptor.Level.BODY)

private val client = OkHttpClient.Builder()
    .addInterceptor(interceptor)
    .readTimeout(120, TimeUnit.SECONDS)
    .build()

val retrofit: Retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .client(client)
    .build()

interface OziRemoteService {

    @POST("v2/users")
    suspend fun registerUser(
        @Query("username") username: String,
        @Query("password") password: String,
        @Query("aviFg") aviFg: Int,
        @Query("aviBg") aviBg: Int,
        @Query("fcmToken") fcmToken: String
    ): JsonObject

    @POST("v2/users/user")
    suspend fun postToUser(
        @Query("data") data: String,
        @Header("x-token") token: String
    )

    @GET("v2/auth/login")
    suspend fun login(
        @Query("username") username: String,
        @Query("password") password: String,
        @Query("fcmToken") fcmToken: String
    ): JsonObject

    @GET("v2/users")
    suspend fun getUsers(
        @Query("requestType") requestType: String,
        @Query("searchTerm") username: String? = null,
        @Query("userId") userId: String? = null,
    ): List<User>

    @POST("v2/chats/chat/messages")
    suspend fun sendMessage(@Query("message") messageJson: String, @Header("x-token") token: String)

    @GET("v2/chats/chat/messages")
    suspend fun getMessages(
        @Query("chatId") chatId: String,
        @Query("lastMessageTimestamp") lastMessageTimestamp: Long?,
        @Query("lastMessageId") lastMessageId: String?,
        @Header("x-token") token: String
    ): List<MessageDto>

    @GET("v2/signals")
    suspend fun getSignals(@Header("x-token") token: String): List<Signal>

    @GET("v2/chats")
    suspend fun getChats(
        @Query("chatId") chatId: String? = null,
        @Header("x-token") token: String
    ): List<ChatDto>

    @POST("v2/gaming")
    suspend fun postToGaming(
        @Query("gaming_data") data: String,
        @Header("x-token") token: String
    ): String

    @GET("ping")
    suspend fun ping(): Boolean
}