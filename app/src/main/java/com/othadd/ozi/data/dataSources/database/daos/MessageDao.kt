package com.othadd.ozi.data.dataSources.database.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.othadd.ozi.domain.model.message.Message
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {

    @Insert
    suspend fun insert(message: Message)

    @Insert
    suspend fun insertMultiple(messages: List<Message>)

    @Delete
    suspend fun delete(message: Message)

    @Delete
    suspend fun deleteMultiple(messages: List<Message>)

    @Query("DELETE from messages")
    suspend fun deleteAll()

    @Update
    suspend fun update(message: Message)

    @Query("SELECT * from messages")
    fun getAllMessages(): Flow<List<Message>>

    @Query("SELECT * from messages WHERE chatId = :chatId")
    fun getMessagesByChatId(chatId: String): Flow<List<Message>>

}