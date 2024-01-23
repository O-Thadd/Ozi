package com.othadd.ozi.di

import com.othadd.ozi.BaseOziApplication
import com.othadd.ozi.common.BASE_URL
import com.othadd.ozi.data.dataSources.database.daos.ChatDao
import com.othadd.ozi.data.dataSources.database.daos.MessageDao
import com.othadd.ozi.data.dataSources.database.daos.NotifHistoryDao
import com.othadd.ozi.data.dataSources.database.daos.UserDao
import com.othadd.ozi.data.dataSources.remote.OziRemoteService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object DataSourcesModule {

    @Provides
    fun provideChatDao(baseOziApp: BaseOziApplication): ChatDao {
        return baseOziApp.database.chatDao()
    }

    @Provides
    fun provideMessageDao(baseOziApp: BaseOziApplication): MessageDao {
        return baseOziApp.database.messageDao()
    }

    @Provides
    fun provideUserDao(baseOziApp: BaseOziApplication): UserDao {
        return baseOziApp.database.userDao()
    }

    @Provides
    fun provideNotifHistoryDao(baseOziApp: BaseOziApplication): NotifHistoryDao {
        return baseOziApp.database.notifHistoryDao()
    }
}