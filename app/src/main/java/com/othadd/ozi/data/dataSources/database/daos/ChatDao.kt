package com.othadd.ozi.data.dataSources.database.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.othadd.ozi.domain.model.chat.Chat
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(chat: Chat)

    @Delete
    suspend fun delete(chats: List<Chat>)

    @Query("DELETE from chats")
    fun deleteAll()

    @Update
    suspend fun update(chat: Chat)

    @Query("SELECT * from chats")
    fun getChats(): Flow<List<Chat>>

    @Query("SELECT * from chats WHERE chatId = :chatId")
    fun getChatFlowByChatId(chatId: String): Flow<Chat?>

    @Query("SELECT * from chats WHERE chatId = :chatId")
    fun getChatByChatId(chatId: String): Chat?
}