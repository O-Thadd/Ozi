package com.othadd.ozi.domain.useCases.defaultImplementations

import android.Manifest
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.MessagingStyle
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.Person
import androidx.core.app.RemoteInput
import androidx.core.graphics.drawable.IconCompat
import androidx.core.net.toUri
import com.othadd.ozi.common.NOTIFICATION_REPLY_TEXT_KEY
import com.othadd.ozi.common.OZI_APP_INTENT_ACTION
import com.othadd.ozi.common.OZI_APP_URI
import com.othadd.ozi.domain.OziReceiver
import com.othadd.ozi.domain.model.NotifHistoryEntry
import com.othadd.ozi.domain.model.NotifHistoryMessage
import com.othadd.ozi.domain.model.NotificationType
import com.othadd.ozi.domain.model.OziNotificationChannel
import com.othadd.ozi.domain.model.User
import com.othadd.ozi.domain.model.message.Message
import com.othadd.ozi.domain.repos.NotifHistoryRepo
import com.othadd.ozi.domain.useCases.interfaces.user.GetUsersUseCases
import com.othadd.ozi.domain.useCases.interfaces.NotificationUseCases
import com.othadd.ozi.domain.useCases.interfaces.message.SendMessageUseCase
import com.othadd.ozi.domain.useCases.interfaces.user.ThisUserUseCases
import com.othadd.ozi.ui.MainActivity
import com.othadd.ozi.ui.chatScreen.CHAT_ID_KEY
import com.othadd.ozi.ui.getBgResourceId
import com.othadd.ozi.ui.getFgResourceId
import com.othadd.ozi.R
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class NotificationUseCasesImpl @Inject constructor(
    private val context: Context,
    private val usersUseCase: GetUsersUseCases,
    private val sendMessageUseCase: SendMessageUseCase,
    private val thisUserUseCases: ThisUserUseCases,
    private val notifHistoryRepo: NotifHistoryRepo
) : NotificationUseCases {

    /**
     * Provide [messageSender] only if the message was sent by this user i.e. an outgoing message
     */
    override suspend fun postNewMessageNotification(
        message: Message,
        channel: OziNotificationChannel,
        context: Context?,
        messageSender: User?,
    ) {
        val appropriateMessageSender = messageSender ?: usersUseCase.getUser(message.senderId)!!
        val appropriateContext = context ?: this.context
        postNotification(
            sender = appropriateMessageSender,
            message = message,
            channel = channel,
            chatId = message.chatId,
            context = appropriateContext,
            type = NotificationType.CHAT
        )
    }

    override suspend fun postGameRequestNotification(sender: User){
        postNotification(
            sender = sender,
            channel = OziNotificationChannel.GAME,
            type = NotificationType.GAME,
            context = context
        )
    }

    override suspend fun clear() {
        with(NotificationManagerCompat.from(context)) {
            cancelAll()
        }
        notifHistoryRepo.wipeEntries()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun replyToNotification(context: Context, chatId: String, reply: String) {
        val newMessageSent = sendMessageUseCase(reply, chatId)
        val messageSender = thisUserUseCases.get().first()
        postNewMessageNotification(
            newMessageSent,
            channel = OziNotificationChannel.SILENT,
            context = context,
            messageSender = messageSender
        )
    }


    private suspend fun postNotification(
        sender: User,
        channel: OziNotificationChannel,
        type: NotificationType,
        context: Context,
        message: Message? = null,
        chatId: String? = null,
    ) {

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        val pendingIntent: PendingIntent?
        val smallIcon: Int
        val title: String
        val text: String
        val notificationId: Int

        when(type){
            NotificationType.CHAT -> {
                smallIcon = R.drawable.ic_app_icon
                title = sender.username
                text = message!!.body
                notificationId = chatId.hashCode()
                pendingIntent = chatId?.let { getActionPendingIntent(it, context) }
            }
            NotificationType.GAME -> {
                smallIcon = R.drawable.ic_game
                title = "New Game Request"
                text = "${sender.username} has challenged you to fastest fingers!"
                notificationId = 1200; //yea!, magic number. feel free to refactor. currently used no where else.
                val intent = Intent(context, MainActivity::class.java)
                pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
            }
        }

        var builder = NotificationCompat.Builder(context, channel.id)
            .setSmallIcon(smallIcon)
            .setContentTitle(title)
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        if (type == NotificationType.CHAT) {
            builder = modifyForChatNotification(builder, message!!, sender, context)
        }

        with(NotificationManagerCompat.from(context)) {
            notify(notificationId, builder.build())
        }
    }

    private fun getActionPendingIntent(chatId: String, context: Context): PendingIntent {
        val deepLinkIntent = Intent(
            Intent.ACTION_VIEW,
            "$OZI_APP_URI/$chatId".toUri(),
            context,
            MainActivity::class.java
        )

        return TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(deepLinkIntent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE)
        }
    }

    private suspend fun modifyForChatNotification(
        builder: NotificationCompat.Builder,
        message: Message,
        sender: User,
        context: Context
    ): NotificationCompat.Builder {
        val replyAction = getReplyAction(context, message.chatId)
        val currentNotifMessage = NotifHistoryMessage(
            sender.username,
            sender.aviFg,
            sender.aviBg,
            message.body,
            message.timestamp
        )
        var entry = notifHistoryRepo.getEntry(message.chatId)
        if (entry == null) {
            entry = NotifHistoryEntry(message.chatId, mutableListOf())
        }
        val messages = entry.messages.toMutableList().also { it.add(currentNotifMessage) }
        val messagingStyle = MessagingStyle("Me")
        addMessages(messagingStyle, messages)

        notifHistoryRepo.addEntry(entry.copy(messages = messages))

        return builder.setStyle(messagingStyle).addAction(replyAction)
    }

    private fun getReplyAction(context: Context, chatId: String): NotificationCompat.Action {
        val replyLabel = "Reply"
        val remoteInput: RemoteInput = RemoteInput.Builder(NOTIFICATION_REPLY_TEXT_KEY).run {
            setLabel(replyLabel)
            build()
        }

        val intent = Intent(context, OziReceiver::class.java).also {
            it.action = OZI_APP_INTENT_ACTION
            it.putExtra(CHAT_ID_KEY, chatId)
        }

        val replyPendingIntent: PendingIntent =
            PendingIntent.getBroadcast(
                context,
                chatId.hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            )

        return NotificationCompat.Action.Builder(
            R.drawable.ic_app_icon,
            replyLabel, replyPendingIntent
        )
            .addRemoteInput(remoteInput)
            .build()
    }

    private fun addMessages(
        messagingStyle: MessagingStyle,
        notifMessages: List<NotifHistoryMessage>
    ) {
        for (message in notifMessages) {
            val icon =
                IconCompat
                    .createWithResource(context, getFgResourceId(message.fg))
                    .setTint(context.getColor(getBgResourceId(message.bg)))
            val person =
                Person.Builder()
                    .setName(message.senderName)
                    .setIcon(icon)
            messagingStyle.addMessage(message.body, message.timestamp, person.build())
        }
    }

}