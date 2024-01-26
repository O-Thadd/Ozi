package com.othadd.ozi.domain.repos

import com.othadd.ozi.domain.model.DataSourcePreference
import com.othadd.ozi.domain.model.OperationOutcome
import com.othadd.ozi.domain.model.message.Message
import kotlinx.coroutines.flow.Flow

interface MessageRepo {

    suspend fun addMessage(message: Message)

    suspend fun sendMessage(message: Message)

    suspend fun postMessage(message: Message)

    suspend fun getMessages(chatId: String): Flow<List<Message>>

    suspend fun getAllMessages(): Flow<List<Message>>

    suspend fun deleteAll()

    /**
     * Syncs all local message with server using the provided chatId . Adding, updating and deleting as appropriate.
     *
     * @return: list of new messages i.e. messages that did not previously exist locally.
     */
    suspend fun syncMessages(
        chatId: String,
        lastMessageTimestamp: Long? = null,
        lastMessageId: String? = null
    ): List<Message>

    suspend fun fetchMessages(
        chatId: String,
        dataSourcePreference: DataSourcePreference,
        lastMessageTimestamp: Long? = null,
        lastMessageId: String? = null
    ): OperationOutcome<List<Message>, Nothing>

}