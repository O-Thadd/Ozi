package com.iyke.ozix

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.iyke.ozix.data.dataSources.database.OziDatabase
import dagger.hilt.InstallIn
import dagger.hilt.android.EarlyEntryPoint
import dagger.hilt.android.EarlyEntryPoints
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.components.SingletonComponent

@HiltAndroidApp
open class OziApplication : BaseOziApplication()

abstract class BaseOziApplication : Application(), Configuration.Provider {
    val database: OziDatabase by lazy { OziDatabase.getDatabase(this) }
    lateinit var workerFactory: HiltWorkerFactory

    @EarlyEntryPoint
    @InstallIn(SingletonComponent::class)
    interface WorkerFactoryEntryPoint {
        fun getFactory(): HiltWorkerFactory
    }

    override fun onCreate() {
        super.onCreate()
        workerFactory = EarlyEntryPoints.get(this, WorkerFactoryEntryPoint::class.java).getFactory()
    }

    override fun getWorkManagerConfiguration(): Configuration = Configuration.Builder()
        .setWorkerFactory(workerFactory)
        .build()
}
