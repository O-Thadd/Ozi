package com.othadd.ozi.di

import android.content.Context
import com.othadd.ozi.BaseOziApplication
import com.othadd.ozi.OziApplication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object CoreModule {

    @Provides
    fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }

    @Provides
    fun provideBaseOziApp(context: Context): BaseOziApplication {
        return context as BaseOziApplication
    }

}