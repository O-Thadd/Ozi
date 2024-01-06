package com.othadd.ozi.domain.workers

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.othadd.ozi.common.WORKER_MESSAGE_KEY
import com.othadd.ozi.common.getOtherParticipantId
import com.othadd.ozi.data.dataSources.remote.OziRemoteService
import com.othadd.ozi.domain.repos.MessageRepo
import com.othadd.ozi.common.stringToMessage
import com.othadd.ozi.domain.repos.UsersRepo
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

/**
 * Sends the message, then syncs the messages; and chatMate if a pair chat.
 */
@HiltWorker
class SendChatMessageWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    val messageRepo: MessageRepo,
    val remoteService: OziRemoteService,
    val usersRepo: UsersRepo
) :
    CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val result: Result = try {
            val message = stringToMessage(inputData.getString(WORKER_MESSAGE_KEY)!!)
            messageRepo.postMessage(message)

            messageRepo.syncMessages(message.chatId)
            if (message.chatId.length > 40) { // checking if it is a pair chat
                val chatMateId = getOtherParticipantId(message.chatId, message.senderId)
                usersRepo.syncUser(chatMateId)
            }

            Result.success()
        } catch (throwable: Throwable) {
            Log.e("Worker send chat message", throwable.message.toString())
            val serverIsReachable: Boolean = try { remoteService.ping() } catch (e: Exception) { false }
            if (serverIsReachable) { Result.failure() } else { Result.retry() }
        }

        return result
    }
}