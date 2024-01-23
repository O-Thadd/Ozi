package com.othadd.ozi.domain.workers

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.othadd.ozi.domain.useCases.interfaces.ProcessSignalsUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class ProcessSignalsWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    val processSignalsUseCase: ProcessSignalsUseCase
) :
    CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            processSignalsUseCase()
            Result.success()
        } catch (throwable: Throwable) {
            Log.e("getWorker", throwable.message.toString())
            Result.failure()
        }
    }
}