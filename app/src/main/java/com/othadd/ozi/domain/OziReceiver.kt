package com.othadd.ozi.domain

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.RemoteInput
import com.othadd.ozi.common.NOTIFICATION_REPLY_TEXT_KEY
import com.othadd.ozi.domain.useCases.interfaces.NotificationUseCases
import com.othadd.ozi.ui.chat.CHAT_ID_KEY
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class OziReceiver: BroadcastReceiver() {

    @Inject lateinit var notificationUseCases: NotificationUseCases
    private val scope = CoroutineScope(Job() + Dispatchers.Default)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context?, intent: Intent?) {
        scope.launch {
            intent ?: return@launch
            context ?: return@launch

            val reply = RemoteInput.getResultsFromIntent(intent)
                ?.getCharSequence(NOTIFICATION_REPLY_TEXT_KEY)
            val chatId = intent.extras?.getString(CHAT_ID_KEY) ?: return@launch
            notificationUseCases.replyToNotification(context, chatId, reply.toString())
        }
    }

}