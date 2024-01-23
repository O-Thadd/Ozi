package com.othadd.ozi.domain.useCases.interfaces

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.othadd.ozi.domain.model.OziNotificationChannel
import com.othadd.ozi.domain.model.User
import com.othadd.ozi.domain.model.message.Message

interface NotificationUseCases {
    /**
     * Provide [messageSender] only if the message was sent by this user i.e. an outgoing message
     */
    suspend fun postNewMessageNotification(
        message: Message,
        channel: OziNotificationChannel,
        context: Context? = null,
        messageSender: User? = null,
    )

    suspend fun postGameRequestNotification(sender: User)
    suspend fun clear()

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun replyToNotification(context: Context, chatId: String, reply: String)
}