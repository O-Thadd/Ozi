package com.othadd.ozi.di

import com.othadd.ozi.ui.model.DefaultSplashScreenSetupRunner
import com.othadd.ozi.ui.model.SplashScreenSetupRunner
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface SplashSetupRunnerModule {

    @Binds
    abstract fun bindSplashRunner(implementation: DefaultSplashScreenSetupRunner): SplashScreenSetupRunner
}