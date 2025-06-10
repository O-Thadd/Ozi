package com.iyke.ozix.di

import com.iyke.ozix.BaseOziApplication
import com.iyke.ozix.data.dataSources.database.daos.ChatDao
import com.iyke.ozix.data.dataSources.database.daos.MessageDao
import com.iyke.ozix.data.dataSources.database.daos.NotifHistoryDao
import com.iyke.ozix.data.dataSources.database.daos.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

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