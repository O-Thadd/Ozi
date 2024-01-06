package com.othadd.ozi.data.dataSources.database.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.othadd.ozi.domain.model.NotifHistoryEntry

@Dao
interface NotifHistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(notifHistory: NotifHistoryEntry)

    @Delete
    suspend fun delete(notifHistory: NotifHistoryEntry)

    @Query("DELETE from notification_history_entries")
    suspend fun deleteAll()

    @Update
    suspend fun update(notifHistory: NotifHistoryEntry)

    @Query("SELECT * from notification_history_entries WHERE chatId = :chatId")
    fun getEntry(chatId: String): NotifHistoryEntry?
}