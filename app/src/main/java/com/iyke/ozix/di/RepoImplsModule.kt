package com.iyke.ozix.di

import com.iyke.ozix.data.repos.AppStateRepoImpl
import com.iyke.ozix.data.repos.ChatRepoImpl
import com.iyke.ozix.data.repos.GamingRepoImpl
import com.iyke.ozix.data.repos.MessageRepoImpl
import com.iyke.ozix.data.repos.NotifHistoryRepoImpl
import com.iyke.ozix.data.repos.SearchHistoryRepoImpl
import com.iyke.ozix.data.repos.SignalRepoImpl
import com.iyke.ozix.data.repos.ThisUserRepoImpl
import com.iyke.ozix.data.repos.UsersRepoImpl
import com.iyke.ozix.domain.repos.AppStateRepo
import com.iyke.ozix.domain.repos.ChatRepo
import com.iyke.ozix.domain.repos.GamingRepo
import com.iyke.ozix.domain.repos.MessageRepo
import com.iyke.ozix.domain.repos.NotifHistoryRepo
import com.iyke.ozix.domain.repos.SearchHistoryRepo
import com.iyke.ozix.domain.repos.SignalRepo
import com.iyke.ozix.domain.repos.ThisUserRepo
import com.iyke.ozix.domain.repos.UsersRepo
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