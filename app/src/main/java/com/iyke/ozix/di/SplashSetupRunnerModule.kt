package com.iyke.ozix.di

import com.iyke.ozix.ui.model.DefaultSplashScreenSetupRunner
import com.iyke.ozix.ui.model.SplashScreenSetupRunner
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