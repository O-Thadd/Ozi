package com.othadd.ozi.di

import com.othadd.ozi.data.dataSources.localStore.DefaultOziDataStore
import com.othadd.ozi.data.dataSources.localStore.OziDataStore
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataStoreModule {

    @Binds
    abstract fun bindOziDataStore(implementation: DefaultOziDataStore): OziDataStore
}