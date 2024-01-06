package com.othadd.ozi.di

import com.othadd.ozi.data.repos.AppStateRepoImpl
import com.othadd.ozi.data.repos.ChatRepoImpl
import com.othadd.ozi.data.repos.GamingRepoImpl
import com.othadd.ozi.data.repos.MessageRepoImpl
import com.othadd.ozi.data.repos.NotifHistoryRepoImpl
import com.othadd.ozi.data.repos.SearchHistoryRepoImpl
import com.othadd.ozi.data.repos.SignalRepoImpl
import com.othadd.ozi.data.repos.ThisUserRepoImpl
import com.othadd.ozi.data.repos.UsersRepoImpl
import com.othadd.ozi.domain.repos.AppStateRepo
import com.othadd.ozi.domain.repos.ChatRepo
import com.othadd.ozi.domain.repos.GamingRepo
import com.othadd.ozi.domain.repos.MessageRepo
import com.othadd.ozi.domain.repos.NotifHistoryRepo
import com.othadd.ozi.domain.repos.SearchHistoryRepo
import com.othadd.ozi.domain.repos.SignalRepo
import com.othadd.ozi.domain.repos.ThisUserRepo
import com.othadd.ozi.domain.repos.UsersRepo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepoImplsModule {

    @Binds
    abstract fun bindAppStateRepo(implementation: AppStateRepoImpl): AppStateRepo

    @Binds
    abstract fun bindChatRepo(implementation: ChatRepoImpl): ChatRepo

    @Binds
    abstract fun bindMessageRepo(implementation: MessageRepoImpl): MessageRepo

    @Binds
    abstract fun bindSearchHistoryRepo(implementation: SearchHistoryRepoImpl): SearchHistoryRepo

    @Binds
    abstract fun bindSignalRepo(implementation: SignalRepoImpl): SignalRepo

    @Binds
    abstract fun bindThisUserRepo(implementation: ThisUserRepoImpl): ThisUserRepo

    @Binds
    abstract fun bindUsersRepo(implementation: UsersRepoImpl): UsersRepo

    @Binds
    abstract fun bindNotifHistoryRepo(implementation: NotifHistoryRepoImpl): NotifHistoryRepo

    @Binds
    abstract fun bindGamingRepo(implementation: GamingRepoImpl): GamingRepo
}