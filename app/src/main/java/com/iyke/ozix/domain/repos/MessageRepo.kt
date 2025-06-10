package com.iyke.ozix.domain.repos

import com.iyke.ozix.data.dataSources.model.OpError
import com.iyke.ozix.domain.model.DataSourcePreference
import com.iyke.ozix.domain.model.OperationOutcomeX
import com.iyke.ozix.domain.model.message.Message
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
    ): OperationOutcomeX<List<Message>, OpError>

}