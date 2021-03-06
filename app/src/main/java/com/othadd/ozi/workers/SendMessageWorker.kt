package com.othadd.ozi.workers

import android.app.Notification
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.othadd.ozi.*
import com.othadd.ozi.database.Converters
import com.othadd.ozi.utils.WORKER_MESSAGE_KEY
import com.othadd.ozi.utils.stringToMessage

class SendMessageWorker(appContext: Context, workerParams: WorkerParameters):
    CoroutineWorker(appContext, workerParams) {

    private val appContext = applicationContext

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return ForegroundInfo(
            MY_NOTIFICATION_ID, createNotification()
        )
    }

    override suspend fun doWork(): Result {
        return try {

            val messagingRepo = MessagingRepo.getInstance(appContext as OziApplication)
            val message = stringToMessage(inputData.getString(WORKER_MESSAGE_KEY)!!)
            messagingRepo.sendMessage(message)
            Result.success()
        } catch (throwable: Throwable){
            Log.e("Worker send message", throwable.message.toString())
            return Result.failure()
        }
    }

    private fun createNotification() : Notification {
        val builder = NotificationCompat.Builder(appContext, MY_NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.notification_image)
            .setContentTitle("New Message!")
            .setContentText("")

        return builder.build()
    }

}