package com.othadd.ozi.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notification_history_entries")
data class NotifHistoryEntry(
    @PrimaryKey val chatId: String,
    val messages: List<NotifHistoryMessage>
)

data class NotifHistoryMessage(
    val senderName: String,
    val fg: Int,
    val bg: Int,
    val body: String,
    val timestamp: Long
)
