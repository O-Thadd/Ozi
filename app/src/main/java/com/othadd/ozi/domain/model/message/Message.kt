package com.othadd.ozi.domain.model.message

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.othadd.ozi.domain.model.User
import java.text.DateFormat
import java.util.Calendar
import java.util.UUID

@Entity(tableName = "messages")
data class Message(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val senderId: String,
    val chatId: String,
    val body: String,
    val timestamp: Long,
    val meta: String = "",

    /**
     * This field is only relevant for sent messages. completely irrelevant for received messages.
     */
    val sent: Boolean = true
)

fun Message.toUIMessage(thisUserId: String, sender: User?): UIMessage {
        val format = DateFormat.getTimeInstance(DateFormat.SHORT)
        val calendarObject = Calendar.getInstance()
        calendarObject.timeInMillis = timestamp
        val date = calendarObject.time

        val senderType = when(senderId) {
            thisUserId -> {
                MessageSender.SELF
            }

            MessageSender.SYSTEM.id -> {
                MessageSender.SYSTEM
            }

            MessageSender.GAME_MODERATOR.id -> {
                MessageSender.GAME_MODERATOR
            }

            else -> {
                MessageSender.CHATMATE
            }
        }

        return UIMessage(id, sender, senderType, body, format.format(date), sent)
}