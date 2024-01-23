package com.othadd.ozi.domain.useCases.implementations.user

import com.othadd.ozi.domain.model.AppState
import com.othadd.ozi.domain.repos.ChatRepo
import com.othadd.ozi.domain.repos.MessageRepo
import com.othadd.ozi.domain.repos.NotifHistoryRepo
import com.othadd.ozi.domain.repos.UsersRepo
import com.othadd.ozi.domain.useCases.interfaces.AppStateUseCases
import com.othadd.ozi.domain.useCases.interfaces.user.LogoutUseCase
import com.othadd.ozi.domain.useCases.interfaces.user.ThisUserUseCases
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LogoutUseCaseImpl @Inject constructor(
    private val appStateUseCases: AppStateUseCases,
    private val messageRepo: MessageRepo,
    private val usersRepo: UsersRepo,
    private val chatRepo: ChatRepo,
    private val thisUserUseCases: ThisUserUseCases,
    private val notifHistoryRepo: NotifHistoryRepo
) : LogoutUseCase {

    override suspend operator fun invoke() {
        withContext(Dispatchers.IO) {
            val currentAppState = appStateUseCases.get()
            appStateUseCases.update(
                AppState.DEFAULT.copy(
                    appStartsCount = currentAppState.appStartsCount,
                    inForeground = currentAppState.inForeground
                )
            )

            thisUserUseCases.updateLocal(null)

            messageRepo.deleteAll()

            usersRepo.deleteAll()

            chatRepo.deleteAll()

            notifHistoryRepo.wipeEntries()

            //Ideally, the fcmToken should be reset with server so that this device stops getting notifications.
            //In this case, it doesn't matter because currently no notifications are sent directly to the system tray.
            //All notifications are processed in the onMessageReceived() method which doesn't run if user state is null.
            //Still, fcmToken reset should really be implemented.
        }
    }
}