package com.othadd.ozi.domain.useCases.defaultImplementations

import com.othadd.ozi.common.JsonResponseField
import com.othadd.ozi.domain.repos.ChatRepo
import com.othadd.ozi.domain.repos.MessageRepo
import com.othadd.ozi.domain.repos.SignalRepo
import com.othadd.ozi.domain.repos.ThisUserRepo
import com.othadd.ozi.domain.repos.UsersRepo
import com.othadd.ozi.domain.model.OperationOutcome
import com.othadd.ozi.domain.model.Signal
import com.othadd.ozi.common.getOtherParticipantId
import com.othadd.ozi.common.stringToStringList
import com.othadd.ozi.domain.gaming.GamingUseCases
import com.othadd.ozi.domain.model.DataSourcePreference
import com.othadd.ozi.domain.model.message.Message
import com.othadd.ozi.domain.model.OziNotificationChannel
import com.othadd.ozi.domain.model.SignalType
import com.othadd.ozi.domain.useCases.interfaces.AppStateUseCases
import com.othadd.ozi.domain.useCases.interfaces.NotificationUseCases
import com.othadd.ozi.domain.useCases.interfaces.ProcessSignalsUseCase
import org.json.JSONObject
import javax.inject.Inject

class ProcessSignalsUseCaseImpl @Inject constructor(
    private val signalRepo: SignalRepo,
    private val chatRepo: ChatRepo,
    private val messageRepo: MessageRepo,
    private val usersRepo: UsersRepo,
    private val thisUserRepo: ThisUserRepo,
    private val notificationUseCases: NotificationUseCases,
    private val appStateUseCase: AppStateUseCases,
    private val gamingUseCases: GamingUseCases
) : ProcessSignalsUseCase {

    override suspend operator fun invoke() {

        val opOutcome = signalRepo.getSignals()
        if (opOutcome is OperationOutcome.Successful) {
            val signals = opOutcome.data!!
            processSignals(signals)
        }
    }

    private suspend fun processSignals(signals: List<Signal>) {
        for (signal in signals) {
            when (SignalType.getSignalType(signal.id)) {

                SignalType.NEW_MESSAGE -> {
                    val newMessages = refreshAllForNewMessages(signal)
                    postNotificationsForNewMessages(newMessages)
                }

                SignalType.GAME_REQUEST -> {
                    if (!appStateUseCase.get().inForeground){
                        (postNotificationForNewGameRequest(signal))
                    }
                    gamingUseCases.handleGameRequest(JSONObject(signal.data))
                }

                SignalType.GAME_BROKER_UPDATE, SignalType.GAME_MODERATOR_UPDATE -> {
                    gamingUseCases.processGameStateUpdate(JSONObject(signal.data))
                }

                null -> { }
            }
        }
    }

    private suspend fun postNotificationForNewGameRequest(signal: Signal) {
        val hostId = JSONObject(signal.data).getString(JsonResponseField.GAME_HOST_ID.string)
        val hostFetchOutcome = usersRepo.getUser(hostId, DataSourcePreference.REMOTE)
        if (hostFetchOutcome is OperationOutcome.Failed) {
            return
        }
        hostFetchOutcome as OperationOutcome.Successful
        hostFetchOutcome.data?.let {
            notificationUseCases.postGameRequestNotification(it)
        }
    }

    private suspend fun postNotificationsForNewMessages(newMessages: MutableList<Message>) {
        if (!appStateUseCase.get().inForeground) {
            for (message in newMessages) {
                notificationUseCases.postNewMessageNotification(
                    message = message,
                    channel = OziNotificationChannel.CHAT
                )
            }
        }
    }

    /**
     * Fetches new messages and refreshes users and chats as necessary.
     * @return New messages that were received i.e. messages that did not previously exist locally.
     */
    private suspend fun refreshAllForNewMessages(signal: Signal): MutableList<Message> {
        val chatIdsJson = signal.data
        val chatIds = stringToStringList(chatIdsJson)
        val thisUserId = thisUserRepo.get()!!.userId
        val newMessages = mutableListOf<Message>()
        for (chatId in chatIds) {
            if (chatId.length > 40) { // checking if it is a pair chat
                val chatMateId = getOtherParticipantId(chatId, thisUserId)
                usersRepo.syncUser(chatMateId)
            }
            chatRepo.syncChat(chatId, true)
            val chatNewMessages = messageRepo.syncMessages(chatId)
            newMessages.addAll(chatNewMessages)
        }
        return newMessages
    }

}