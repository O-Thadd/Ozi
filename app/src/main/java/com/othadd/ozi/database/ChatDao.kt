package com.othadd.ozi.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {

    @Insert
    suspend fun insert(dbChat: DBChat)

    @Delete
    suspend fun delete(dbChat: DBChat)

    @Update
    suspend fun update(dbChat: DBChat)

    @Query("SELECT * from chat")
    fun getChats(): Flow<List<DBChat>>

    @Query("SELECT * from chat WHERE chatMateId = :chatMateId")
    fun getChatByChatmateId(chatMateId: String): Flow<DBChat>

    @Query("SELECT * from chat WHERE chatMateUsername = :chatMateUsername")
    fun getChatByChatmateUsername(chatMateUsername: String): Flow<DBChat>
}