package com.othadd.ozi.di

import com.othadd.ozi.OziApplication
import com.othadd.ozi.data.dataSources.database.daos.ChatDao
import com.othadd.ozi.data.dataSources.database.daos.MessageDao
import com.othadd.ozi.data.dataSources.database.daos.NotifHistoryDao
import com.othadd.ozi.data.dataSources.database.daos.UserDao
import com.othadd.ozi.data.dataSources.remote.OziRemoteService
import com.othadd.ozi.data.dataSources.remote.retrofit
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DataSourcesModule {

    @Provides
    fun provideChatDao(oziApp: OziApplication): ChatDao {
        return oziApp.database.chatDao()
    }

    @Provides
    fun provideMessageDao(oziApp: OziApplication): MessageDao {
        return oziApp.database.messageDao()
    }

    @Provides
    fun provideUserDao(oziApp: OziApplication): UserDao {
        return oziApp.database.userDao()
    }

    @Provides
    fun provideNotifHistoryDao(oziApp: OziApplication): NotifHistoryDao {
        return oziApp.database.notifHistoryDao()
    }

    @Provides
    fun provideRemoteService(): OziRemoteService {
        val retrofitService: OziRemoteService by lazy {
            retrofit.create(OziRemoteService::class.java)
        }
        return retrofitService
    }
}