package com.iyke.ozix.di

import com.iyke.ozix.domain.useCases.defaultImplementations.AppStateUseCasesImpl
import com.iyke.ozix.domain.useCases.defaultImplementations.GetFormattedDateTimeUseCaseImpl
import com.iyke.ozix.domain.useCases.defaultImplementations.NotificationUseCasesImpl
import com.iyke.ozix.domain.useCases.defaultImplementations.ProcessSignalsUseCaseImpl
import com.iyke.ozix.domain.useCases.defaultImplementations.chat.CreatePairChatUseCaseImpl
import com.iyke.ozix.domain.useCases.defaultImplementations.chat.DeleteChatsUseCaseImpl
import com.iyke.ozix.domain.useCases.defaultImplementations.chat.GetChatsUseCaseImpl
import com.iyke.ozix.domain.useCases.defaultImplementations.chat.GetSingleChatUseCaseImpl
import com.iyke.ozix.domain.useCases.defaultImplementations.message.GetMessagesUseCaseImpl
import com.iyke.ozix.domain.useCases.defaultImplementations.message.MarkMessagesReadUseCaseImpl
import com.iyke.ozix.domain.useCases.defaultImplementations.message.SendMessageUseCaseImpl
import com.iyke.ozix.domain.useCases.defaultImplementations.user.GetUsersUseCasesImpl
import com.iyke.ozix.domain.useCases.defaultImplementations.user.LoginUserUseCaseImpl
import com.iyke.ozix.domain.useCases.defaultImplementations.user.LogoutUseCaseImpl
import com.iyke.ozix.domain.useCases.defaultImplementations.user.RegisterUserUseCaseImpl
import com.iyke.ozix.domain.useCases.defaultImplementations.user.ThisUserUseCasesImpl
import com.iyke.ozix.domain.useCases.interfaces.AppStateUseCases
import com.iyke.ozix.domain.useCases.interfaces.chat.CreatePairChatUseCase
import com.iyke.ozix.domain.useCases.interfaces.chat.DeleteChatsUseCase
import com.iyke.ozix.domain.useCases.interfaces.chat.GetChatsUseCase
import com.iyke.ozix.domain.useCases.interfaces.GetFormattedDateTimeUseCase
import com.iyke.ozix.domain.useCases.interfaces.message.GetMessagesUseCase
import com.iyke.ozix.domain.useCases.interfaces.chat.GetSingleChatUseCase
import com.iyke.ozix.domain.useCases.interfaces.user.GetUsersUseCases
import com.iyke.ozix.domain.useCases.interfaces.user.LoginUserUseCase
import com.iyke.ozix.domain.useCases.interfaces.user.LogoutUseCase
import com.iyke.ozix.domain.useCases.interfaces.message.MarkMessagesReadUseCase
import com.iyke.ozix.domain.useCases.interfaces.NotificationUseCases
import com.iyke.ozix.domain.useCases.interfaces.ProcessSignalsUseCase
import com.iyke.ozix.domain.useCases.interfaces.user.RegisterUserUseCase
import com.iyke.ozix.domain.useCases.interfaces.message.SendMessageUseCase
import com.iyke.ozix.domain.useCases.interfaces.user.ThisUserUseCases
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface UseCasesModule {

    @Binds
    abstract fun bindAppStateUseCases(implementation: AppStateUseCasesImpl): AppStateUseCases

    @Binds
    abstract fun bindCreatePairChatUseCase(implementation: CreatePairChatUseCaseImpl): CreatePairChatUseCase

    @Binds
    abstract fun bindDeleteChatsUseCase(implementation: DeleteChatsUseCaseImpl): DeleteChatsUseCase

    @Binds
    abstract fun bindGetChatsUseCase(implementation: GetChatsUseCaseImpl): GetChatsUseCase

    @Binds
    abstract fun bindFormattedDateTimeUseCase(implementation: GetFormattedDateTimeUseCaseImpl): GetFormattedDateTimeUseCase

    @Binds
    abstract fun bindGetMessagesUseCase(implementation: GetMessagesUseCaseImpl): GetMessagesUseCase

    @Binds
    abstract fun bindGetSingleChatUseCase(implementation: GetSingleChatUseCaseImpl): GetSingleChatUseCase

    @Binds
    abstract fun bindGetUsersUseCase(implementation: GetUsersUseCasesImpl): GetUsersUseCases

    @Binds
    abstract fun bindLoginUseCase(implementation: LoginUserUseCaseImpl): LoginUserUseCase

    @Binds
    abstract fun bindLogoutUseCase(implementation: LogoutUseCaseImpl): LogoutUseCase

    @Binds
    abstract fun bindMarkMessagesReadUseCase(implementation: MarkMessagesReadUseCaseImpl): MarkMessagesReadUseCase

    @Binds
    abstract fun bindNotificationUseCase(implementation: NotificationUseCasesImpl): NotificationUseCases

    @Binds
    abstract fun bindProcessSignalsUseCase(implementation: ProcessSignalsUseCaseImpl): ProcessSignalsUseCase

    @Binds
    abstract fun bindRegisterUserUseCase(implementation: RegisterUserUseCaseImpl): RegisterUserUseCase

    @Binds
    abstract fun bindSendMessageUseCase(implementation: SendMessageUseCaseImpl): SendMessageUseCase

    @Binds
    abstract fun bindThisUserUseCases(implementation: ThisUserUseCasesImpl): ThisUserUseCases
}