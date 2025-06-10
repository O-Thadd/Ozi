package com.iyke.ozix.di

import com.iyke.ozix.data.dataSources.localStore.DefaultOziDataStore
import com.iyke.ozix.data.dataSources.localStore.OziDataStore
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