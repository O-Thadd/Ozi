package com.iyke.ozix.di

import com.iyke.ozix.common.BASE_URL
import com.iyke.ozix.data.dataSources.localStore.OziDataStore
import com.iyke.ozix.data.dataSources.remote.OziRemoteService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Inject

@Module
@InstallIn(SingletonComponent::class)
object RemoteServiceModule {

    @Provides
    fun provideRemoteService(authInterceptor: OziAuthInterceptor): OziRemoteService {

        val loggingInterceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
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

class OziAuthInterceptor @Inject constructor(val dataStore: OziDataStore): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = chain.request().newBuilder()
        val userId = runBlocking { dataStore.getThisUserFlow().first()?.userId }
        if (userId != null) {
            newRequest.addHeader("Authorization", "Bearer $userId")
        }
        return chain.proceed(newRequest.build())
    }
}