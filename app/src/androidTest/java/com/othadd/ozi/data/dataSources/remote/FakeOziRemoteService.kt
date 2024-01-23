package com.othadd.ozi.data.dataSources.remote

import android.app.GameState
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.othadd.ozi.common.STATUS_FAILURE
import com.othadd.ozi.common.STATUS_SUCCESS
import com.othadd.ozi.data.dataSources.model.RemoteUsersRequestType
import com.othadd.ozi.domain.model.Signal
import com.othadd.ozi.domain.model.User
import com.othadd.ozi.domain.model.chat.Chat
import com.othadd.ozi.domain.model.chat.ChatDto
import com.othadd.ozi.domain.model.gaming.GamePrepState
import com.othadd.ozi.domain.model.gaming.UserGameState
import com.othadd.ozi.domain.model.message.Message
import com.othadd.ozi.domain.model.message.MessageDto
import com.othadd.ozi.domain.model.message.toMessage
import com.othadd.ozi.testUser1
import com.othadd.ozi.testUser2
import com.othadd.ozi.testUser3
import com.othadd.ozi.testUser4
import kotlinx.coroutines.delay
import java.util.UUID

class FakeOziRemoteService : OziRemoteService {
    private var networkError = false
    private var failServerResponse = false

    private val users = mutableListOf<User>()
    private val messages = mutableListOf<MessageDto>()
    private val chats = mutableListOf<Chat>()

    override suspend fun registerUser(
        username: String,
        password: String,
        aviFg: Int,
        aviBg: Int,
        fcmToken: String
    ): JsonObject {
        delay(2_000)

        if (networkError) {
            throw Exception("fake network error")
        }

        val response = JsonObject()
        if (failServerResponse || users.any { it.username == username }) {
            response.addProperty("status", STATUS_FAILURE)
        } else {
            val userId = UUID.randomUUID().toString()
            val userToken = "testUserToken"
            response.addProperty("status", STATUS_SUCCESS)
            response.addProperty("userId", userId)
            response.addProperty("token", userToken)
            users.add(
                User(
                    userId,
                    username,
                    aviFg,
                    aviBg,
                    true,
                    false,
                    userToken,
                    UserGameState.AVAILABLE.string
                )
            )
        }

        return response
    }

    override suspend fun postToUser(data: String, token: String) {}

    override suspend fun login(username: String, password: String, fcmToken: String): JsonObject {
        delay(2_000)

        if (networkError) {
            throw Exception("fake network error")
        }

        val response = JsonObject()
        if (failServerResponse) {
            response.addProperty("status", STATUS_FAILURE)
        } else {
            val user = users.find { it.username == username } ?: User(
                UUID.randomUUID().toString(),
                username,
                1,
                1,
                true,
                false,
                "testUserToken",
                ""
            )
            response.addProperty("status", STATUS_SUCCESS)
            response.addProperty("user", Gson().toJson(user))
        }

        return response
    }

    override suspend fun getUsers(
        requestType: String,
        username: String?,
        userId: String?
    ): List<User> {
        delay(2_000)
        return when (RemoteUsersRequestType.getType(requestType)) {
            RemoteUsersRequestType.SPECIFIC -> users.find { it.userId == userId }?.let { listOf(it) } ?: emptyList()
            RemoteUsersRequestType.SUGGESTED -> users
            RemoteUsersRequestType.SEARCH ->users.filter { it.username.lowercase().contains(username!!.lowercase()) }
            null -> emptyList()
        }
    }

    override suspend fun sendMessage(messageJson: String, token: String) {
        val message = Gson().fromJson(messageJson, Message::class.java)
        messages.add(message.toMessageDto())
        if (chats.none { it.chatId == message.chatId }){
            chats.add(Chat(message.chatId, listOf(message.chatId.substring(0..40), message.chatId.substring(41)), false))
        }
    }

    override suspend fun getMessages(
        chatId: String,
        lastMessageTimestamp: Long?,
        lastMessageId: String?,
        token: String
    ): List<MessageDto> {
        delay(2_000)
        return messages.filter { it.chatId == chatId }
    }

    override suspend fun getSignals(token: String): List<Signal> {
        val signal1 = Signal("signal1id", "singal1name", 1L, "")
        val signal2 = Signal("signal2id", "singal2name", 2L, "")
        return listOf(signal1, signal2)
    }

    override suspend fun getChats(chatId: String?, token: String): List<ChatDto> {
        delay(2_000)
        return chats.map { it.toChatDto() }
    }

    override suspend fun postToGaming(data: String, token: String): String {
        return ""
    }

    override suspend fun ping(): Boolean {
        return !networkError
    }

    init {
        users.addAll(listOf(testUser1, testUser2, testUser3))
    }

    private fun Message.toMessageDto(): MessageDto {
        return MessageDto(id, senderId, chatId, body, timestamp, meta)
    }

    private fun Chat.toChatDto(): ChatDto {
        return ChatDto(chatId, participantIds)
    }
}