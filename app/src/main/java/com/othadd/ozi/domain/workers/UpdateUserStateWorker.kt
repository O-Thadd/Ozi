package com.othadd.ozi.domain.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import com.othadd.ozi.common.WORKER_USER_KEY
import com.othadd.ozi.data.dataSources.remote.OziRemoteService
import com.othadd.ozi.domain.model.User
import com.othadd.ozi.domain.useCases.user.ThisUserUseCases
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class UpdateUserStateWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    val thisUserUseCases: ThisUserUseCases,
    val remoteService: OziRemoteService,
): CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            val user = Gson().fromJson(inputData.getString(WORKER_USER_KEY), User::class.java)
            thisUserUseCases.updateRemote(user)
            Result.success()
        } catch (e: Exception) {
            val serverIsReachable: Boolean = try { remoteService.ping() } catch (e: Exception) { false }
            if (serverIsReachable) { Result.failure() } else { Result.retry() }
        }
    }
}