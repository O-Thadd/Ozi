package com.othadd.ozi.di

import com.othadd.ozi.OziApplication
import com.othadd.ozi.common.BASE_URL
import com.othadd.ozi.data.dataSources.database.daos.ChatDao
import com.othadd.ozi.data.dataSources.database.daos.MessageDao
import com.othadd.ozi.data.dataSources.database.daos.NotifHistoryDao
import com.othadd.ozi.data.dataSources.database.daos.UserDao
import com.othadd.ozi.data.dataSources.localStore.DefaultOziDataStore
import com.othadd.ozi.data.dataSources.localStore.OziDataStore
import com.othadd.ozi.data.dataSources.remote.OziRemoteService
import dagger.Binds
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

        val interceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        val retrofit: Retrofit = Retrofit.Builder()
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(client)
            .build()

        val retrofitService: OziRemoteService by lazy {
            retrofit.create(OziRemoteService::class.java)
        }

        return retrofitService
    }
}