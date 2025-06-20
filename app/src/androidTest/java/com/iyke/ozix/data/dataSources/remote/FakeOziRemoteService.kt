package com.iyke.ozix.data.dataSources.remote

import com.iyke.ozix.data.dataSources.model.ApiResponse
import com.iyke.ozix.data.dataSources.model.CreateUserResponse
import com.iyke.ozix.data.dataSources.model.LoginPayload
import com.iyke.ozix.data.dataSources.model.OziResponse
import com.iyke.ozix.data.dataSources.model.RegisterUserPayload
import com.iyke.ozix.data.repos.SendMessageResponse
import com.iyke.ozix.domain.model.FromApiUser
import com.iyke.ozix.domain.model.Signal
import com.iyke.ozix.domain.model.ToApiUser
import com.iyke.ozix.domain.model.User
import com.iyke.ozix.domain.model.chat.ApiChat
import com.iyke.ozix.domain.model.chat.Chat
import com.iyke.ozix.domain.model.chat.ChatDto
import com.iyke.ozix.domain.model.message.FromApiMessage
import com.iyke.ozix.domain.model.message.Message
import com.iyke.ozix.domain.model.message.ToApiMessage
import com.iyke.ozix.testUser1
import com.iyke.ozix.testUser2
import com.iyke.ozix.testUser3
import kotlinx.coroutines.delay
import retrofit2.Response
import java.util.UUID

class FakeOziRemoteService : OziRemoteService {
    private var networkError = false
    private var failServerResponse = false

    private val users = mutableListOf<User>()
    private val messages = mutableListOf<FromApiMessage>()
    private val chats = mutableListOf<Chat>()

    override suspend fun registerUser(payload: RegisterUserPayload): OziResponse<CreateUserResponse> {
        delay(2_000)
        return CreateUserResponse(UUID.randomUUID().toString()).wrapInOziResponse()
    }

    override suspend fun updateUser(userId: String, user: ToApiUser): OziResponse<Unit> {
        delay(2_000)
        return Unit.wrapInOziResponse()
    }

    override suspend fun login(payload: LoginPayload): OziResponse<FromApiUser> {
        delay(2_000)
        return FromApiUser("testusername", "testuserid", 1, 1,).wrapInOziResponse()
    }

    override suspend fun getAllUsers(): OziResponse<List<FromApiUser>> {
        delay(2_000)
        return users.map { it.toFromApiUser() }.wrapInOziResponse()
    }

    override suspend fun searchUsers(username: String): OziResponse<List<FromApiUser>> {
        delay(2_000)
        return users.filter { it.username.contains(username) }.map { it.toFromApiUser() }.wrapInOziResponse()
    }

    override suspend fun getUser(userId: String): OziResponse<FromApiUser> {
        delay(2_000)
        return (users.find { it.userId == userId } ?: testUser1).toFromApiUser().wrapInOziResponse()
    }

    override suspend fun sendMessage(message: ToApiMessage): OziResponse<SendMessageResponse> {
        delay(2_000)
        return SendMessageResponse(UUID.randomUUID().toString(), UUID.randomUUID().toString()).wrapInOziResponse()
    }

    override suspend fun getMessages(chatId: String): OziResponse<List<FromApiMessage>> {
        delay(2_000)
        return messages.filter { it.chatId == chatId }.wrapInOziResponse()
    }

    override suspend fun getSignals(token: String): List<Signal> {
        val signal1 = Signal("signal1id", "singal1name", 1L, "")
        val signal2 = Signal("signal2id", "singal2name", 2L, "")
        return listOf(signal1, signal2)
    }

    override suspend fun getUserChats(): OziResponse<List<ApiChat>> {
        TODO("Not yet implemented")
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

    private fun Message.toMessageDto(): FromApiMessage {
        return FromApiMessage(id, senderId, chatId, body, timestamp, meta)
    }

    private fun Chat.toChatDto(): ChatDto {
        return ChatDto(chatId, participantIds)
    }

    private fun <T> T.wrapInOziResponse(successful: Boolean = true) =
        Response.success(ApiResponse(successful, null, this))

    private fun User.toFromApiUser() = FromApiUser(username, userId, aviFg, aviBg)
}