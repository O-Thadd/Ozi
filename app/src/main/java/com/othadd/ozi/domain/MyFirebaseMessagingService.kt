package com.othadd.ozi.domain

import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.othadd.ozi.domain.workers.ProcessSignalsWorker


class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        val workManager = WorkManager.getInstance(this)
        workManager.enqueue(OneTimeWorkRequest.from(ProcessSignalsWorker::class.java))
    }

    override fun onNewToken(token: String) {  }
}