package com.othadd.ozi.data.dataSources

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.othadd.ozi.common.STATUS_FAILURE
import com.othadd.ozi.common.STATUS_SUCCESS
import com.othadd.ozi.data.dataSources.model.RemoteUsersRequestType
import com.othadd.ozi.data.dataSources.remote.OziRemoteService
import com.othadd.ozi.domain.model.Signal
import com.othadd.ozi.domain.model.User
import com.othadd.ozi.domain.model.chat.Chat
import com.othadd.ozi.domain.model.chat.ChatDto
import com.othadd.ozi.domain.model.message.Message
import com.othadd.ozi.domain.model.message.MessageDto
import com.othadd.ozi.testUser1
import com.othadd.ozi.testUser2
import com.othadd.ozi.testUser3
import com.othadd.ozi.testUser4
import kotlinx.coroutines.delay

class FakeOziRemoteService: OziRemoteService {
    private var networkError = false
    private var failServerResponse = false

    override suspend fun registerUser(
        username: String,
        password: String,
        aviFg: Int,
        aviBg: Int,
        fcmToken: String
    ): JsonObject {
        delay(2_000)
        if (networkError){
            throw Exception("fake network error")
        }

        val response = JsonObject()
        if (failServerResponse){
            response.addProperty("status", STATUS_FAILURE)
        }
        else{
            response.addProperty("status", STATUS_SUCCESS)
            response.addProperty("userId", "testUserId")
            response.addProperty("token", "testUserToken")
        }

        return response
    }

    override suspend fun postToUser(data: String, token: String) { }

    override suspend fun login(username: String, password: String, fcmToken: String): JsonObject {
        delay(2_000)
        if (networkError){
            throw Exception("fake network error")
        }

        val response = JsonObject()
        if (failServerResponse){
            response.addProperty("status", STATUS_FAILURE)
        }
        else{
            response.addProperty("status", STATUS_SUCCESS)
            val user = User("testUserId", username, 1, 1, true, false, "testUserToken", "")
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
        return when(RemoteUsersRequestType.getType(requestType)){
            RemoteUsersRequestType.SPECIFIC -> listOf(testUser1.copy(userId = userId!!))
            RemoteUsersRequestType.SUGGESTED -> listOf(testUser1, testUser2)
            RemoteUsersRequestType.SEARCH -> listOf(testUser3, testUser4)
            null -> emptyList()
        }
    }

    override suspend fun sendMessage(messageJson: String, token: String) {  }

    override suspend fun getMessages(
        chatId: String,
        lastMessageTimestamp: Long?,
        lastMessageId: String?,
        token: String
    ): List<MessageDto> {
        delay(2_000)
        val message1 = Message(
            senderId = "sender1id",
            chatId = "chat1id",
            body = "message1body",
            timestamp = 1L
        )
        val message2 = Message(
            senderId = "sender2id",
            chatId = "chat2id",
            body = "message2body",
            timestamp = 2L
        )
        val message3 = Message(
            senderId = "sender3id",
            chatId = "chat3id",
            body = "message3body",
            timestamp = 3L
        )
        val messages = listOf(message1, message2, message3)
        return messages.map { it.toMessageDto() }
    }

    override suspend fun getSignals(token: String): List<Signal> {
        val signal1 = Signal("signal1id", "singal1name", 1L, "")
        val signal2 = Signal("signal2id", "singal2name", 2L, "")
        return listOf(signal1, signal2)
    }

    override suspend fun getChats(chatId: String?, token: String): List<ChatDto> {
        delay(2_000)
        val chat1 = Chat("chat1", listOf("id1, id2"), false)
        val chat2 = Chat("chat2", listOf("id1, id2"), false)
        return listOf(chat1, chat2).map { it.toChatDto() }
    }

    override suspend fun postToGaming(data: String, token: String): String {
        return ""
    }

    override suspend fun ping(): Boolean {
        return !networkError
    }


    private fun Message.toMessageDto(): MessageDto{
        return MessageDto(id, senderId, chatId, body, timestamp, meta)
    }

    private fun Chat.toChatDto(): ChatDto {
        return ChatDto(chatId, participantIds)
    }
}