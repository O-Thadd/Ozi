package com.othadd.ozi.domain.gaming

import com.othadd.ozi.common.GAME_REQUEST_TIMEOUT_DURATION_MILLIS
import com.othadd.ozi.common.JsonResponseField
import com.othadd.ozi.common.stringToStringList
import com.othadd.ozi.domain.model.AppState
import com.othadd.ozi.domain.model.DataSourcePreference
import com.othadd.ozi.domain.model.OperationOutcome
import com.othadd.ozi.domain.model.User
import com.othadd.ozi.domain.model.gaming.GamePrepOutcome
import com.othadd.ozi.domain.model.gaming.GamePrepOutcome.CANCELLING_NO_HOST_RESPONSE
import com.othadd.ozi.domain.model.gaming.GamePrepOutcome.HOST_CANCELLED
import com.othadd.ozi.domain.model.gaming.GamePrepOutcome.PROMPTING_HOST
import com.othadd.ozi.domain.model.gaming.GamePrepOutcome.STARTING_GAME
import com.othadd.ozi.domain.model.gaming.GamePrepOutcome.ZERO_ACCEPTANCE
import com.othadd.ozi.domain.model.gaming.GamePrepState
import com.othadd.ozi.domain.repos.AppStateRepo
import com.othadd.ozi.domain.repos.ChatRepo
import com.othadd.ozi.domain.repos.GamingRepo
import com.othadd.ozi.domain.repos.ThisUserRepo
import com.othadd.ozi.domain.repos.UsersRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GamingUseCases @Inject constructor(
    private val gamingRepo: GamingRepo,
    private val thisUserRepo: ThisUserRepo,
    private val usersRepo: UsersRepo,
    private val appStateRepo: AppStateRepo,
    private val chatRepo: ChatRepo
) {

    private val timerScope = CoroutineScope(Job() + Dispatchers.Main)
    private var activeTimerJob: Job? = null

    suspend fun sendGameRequest(participantsIds: List<String>): OperationOutcome<Nothing, Nothing> {
        return try {
            val invitees = fetchInvitees(participantsIds)
            val response = gamingRepo.sendGamingRequest(participantsIds)
            val responseJson = JSONObject(response)
            val brokerId = responseJson.getString(JsonResponseField.GAME_BROKER_ID.string)
            val requestCreationTime =
                responseJson.getLong(JsonResponseField.GAME_REQUEST_CREATION_TIME.string)
            val thisUser = thisUserRepo.get()!!
            val gameState = GamePrepState(
                requestCreationTime,
                thisUser,
                thisUser.userId,
                invitees,
                emptyList(),
                emptyList(),
                null
            )

            appStateRepo.update(
                getAppState().copy(gameBrokerId = brokerId, gamePrepState = gameState)
            )

            activeTimerJob?.cancel()
            activeTimerJob = timerScope.launch(Job()) { startTimer(requestCreationTime) }

            OperationOutcome.Successful()
        } catch (e: Exception) {
            OperationOutcome.Failed()
        }
    }

    suspend fun acceptGame() {
        gamingRepo.acceptGamingRequest()
    }

    suspend fun declineGame() {
        activeTimerJob?.cancel()
        appStateRepo.update(getAppState().copy(gamePrepState = null))
        gamingRepo.declineGamingRequest()
        appStateRepo.update(getAppState().copy(gameBrokerId = null))
    }

    suspend fun proceed() {
        appStateRepo.update(getAppState().copy(gamePrepState = null))
        gamingRepo.proceed()
    }

    suspend fun cancelGame() {
        appStateRepo.update(getAppState().copy(gamePrepState = null))
        gamingRepo.cancelGame()
        appStateRepo.update(getAppState().copy(gameBrokerId = null))
    }

    suspend fun processGameStateUpdate(updateJson: JSONObject) {
        val handled = processModeratorUpdate(updateJson)
        if (handled) {
            return
        }
        processBrokerUpdate(updateJson)
    }

    private suspend fun processModeratorUpdate(updateJson: JSONObject): Boolean {
        val gameEnded = try {
            updateJson.getBoolean("game_ended")
        } catch (e: Exception) {
            null
        }

        gameEnded ?: return false

        if (gameEnded) {
            CoroutineScope(Dispatchers.Main).launch {
                delay(3000)
                resetGamingState()
            }
        }
        return true
    }

    private suspend fun processBrokerUpdate(updateJson: JSONObject) {
        val acceptingInvitees =
            try {
                stringToStringList(updateJson.getString(JsonResponseField.ACCEPTING_GAME_INVITEES.string))
            } catch (e: Exception) {
                null
            }
        val decliningInvitees =
            try {
                stringToStringList(updateJson.getString(JsonResponseField.DECLINING_GAME_INVITEES.string))
            } catch (e: Exception) {
                null
            }
        val outcome =
            try {
                GamePrepOutcome.getOutcome(updateJson.getString(JsonResponseField.GAME_BROKER_OUTCOME.string))
            } catch (e: Exception) {
                null
            }

        val currentState = getGameState()!!
        val newAcceptingInvitees = acceptingInvitees ?: currentState.acceptedInviteesIds
        val newDeclinedInvitees = decliningInvitees ?: currentState.declinedInviteesIds
        val newGameState = getGameState()!!.copy(
            acceptedInviteesIds = newAcceptingInvitees,
            declinedInviteesIds = newDeclinedInvitees,
            prepOutcome = outcome
        )
        appStateRepo.update(getAppState().copy(gamePrepState = newGameState))

        if (allAvailableInviteesHaveDeclined(updateJson)) {
            delay(2_000)
            appStateRepo.update(
                getAppState()
                    .copy(gamePrepState = newGameState.copy(prepOutcome = ZERO_ACCEPTANCE))
            )
            delay(1_000)
            appStateRepo.update(
                getAppState().copy(gameBrokerId = "", gamePrepState = null)
            )
        }

        handleOutcome(outcome, updateJson)
    }

    private suspend fun handleOutcome(outcome: GamePrepOutcome?, updateJson: JSONObject) {
        when (outcome) {
            PROMPTING_HOST, ZERO_ACCEPTANCE, null -> {}

            CANCELLING_NO_HOST_RESPONSE, HOST_CANCELLED -> {
                delay(2_000)
                resetGamingState()
            }

            STARTING_GAME -> {
                val gameChatId = updateJson.getString(JsonResponseField.GAME_CHAT_ID.string)
                val gameModeratorId =
                    updateJson.getString(JsonResponseField.GAME_MODERATOR_ID.string)
                chatRepo.syncChat(gameChatId)
                appStateRepo.update(
                    getAppState().copy(
                        gamePrepState = null,
                        gameBrokerId = null,
                        gameModeratorId = gameModeratorId,
                        gameChatId = gameChatId
                    )
                )
            }
        }
    }

    suspend fun handleGameRequest(dataJson: JSONObject) {

        val requestCreationTime =
            dataJson.getLong(JsonResponseField.GAME_REQUEST_CREATION_TIME.string)
        val hostId = dataJson.getString(JsonResponseField.GAME_HOST_ID.string)
        val hostFetchOutcome = usersRepo.getUser(hostId, DataSourcePreference.REMOTE)
        if (hostFetchOutcome is OperationOutcome.Failed) return

        val host = (hostFetchOutcome as OperationOutcome.Successful).data!!
        val thisUserId = thisUserRepo.get()!!.userId
        val inviteesId =
            stringToStringList(dataJson.getString(JsonResponseField.GAME_INVITEES.string))
        val invitees = fetchInvitees(inviteesId)

        val brokerId = dataJson.getString(JsonResponseField.GAME_BROKER_ID.string)
        appStateRepo.update(getAppState().copy(gameBrokerId = brokerId))

        val gameState = GamePrepState(
            requestCreationTime = requestCreationTime,
            host = host,
            thisUserId = thisUserId,
            invitees = invitees,
            declinedInviteesIds = emptyList(),
            acceptedInviteesIds = emptyList(),
            prepOutcome = null
        )

        appStateRepo.update(
            getAppState().copy(gameBrokerId = brokerId, gamePrepState = gameState)
        )

        activeTimerJob?.cancel()
        activeTimerJob = timerScope.launch(Job()) { startTimer(requestCreationTime) }
    }

    suspend fun resetGamingState() {
        appStateRepo.update(
            getAppState().copy(
                gameBrokerId = null,
                gamePrepState = null,
                gameChatId = null,
                gameModeratorId = null
            )
        )
        activeTimerJob?.cancel()
        val gameChats = chatRepo.getChats().first().filter { it.chatId.length < 40 }
        chatRepo.deleteChats(gameChats)
    }

    private suspend fun startTimer(creationTime: Long) {
        val timeToFinish = creationTime + GAME_REQUEST_TIMEOUT_DURATION_MILLIS
        val timeLeft = timeToFinish - Calendar.getInstance().timeInMillis
        delay(timeLeft)
        onLocalTimeout()
    }

    private suspend fun onLocalTimeout() {
        getGameState() ?: return

        val thisUserIsHost = getThisUser()!!.userId == getGameState()!!.host.userId
        if (thisUserIsHost) {
            if (getGameState()!!.acceptedInviteesIds.isEmpty()) {
                appStateRepo.update(
                    getAppState().copy(
                        gamePrepState = getGameState()?.copy(
                            prepOutcome = ZERO_ACCEPTANCE
                        )
                    )
                )
                delay(2_000)
                resetGamingState()
            }
        } else {
            if (getThisUser()!!.userId !in getGameState()!!.acceptedInviteesIds) {
                resetGamingState()
            }
        }
    }

    private suspend fun allAvailableInviteesHaveDeclined(dataJson: JSONObject): Boolean {
        val currentGameState = getGameState()!!
        val currentAcceptingInvitees = currentGameState.acceptedInviteesIds
        val currentDecliningInvitees = currentGameState.declinedInviteesIds

        val acceptingInviteesIds =
            try {
                stringToStringList(dataJson.getString(JsonResponseField.ACCEPTING_GAME_INVITEES.string))
            } catch (e: Exception) {
                currentAcceptingInvitees
            }
        val decliningInvitees =
            try {
                stringToStringList(dataJson.getString(JsonResponseField.DECLINING_GAME_INVITEES.string))
            } catch (e: Exception) {
                currentDecliningInvitees
            }

        val currentPendingInvitees =
            currentGameState.invitees.filter { it.userId !in acceptingInviteesIds && it.userId !in decliningInvitees }
        val pendingInviteesIds =
            try {
                stringToStringList(dataJson.getString(JsonResponseField.PENDING_GAME_INVITEES.string))
            } catch (e: Exception) {
                currentPendingInvitees
            }

        return pendingInviteesIds.isEmpty() && acceptingInviteesIds.isEmpty()
    }

    private suspend fun fetchInvitees(
        participantsIds: List<String>
    ): List<User> {
        val invitees = mutableListOf<User>()
        for (userId in participantsIds) {
            val outcome = usersRepo.getUser(userId, DataSourcePreference.REMOTE)
            if (outcome is OperationOutcome.Failed) {
                throw Exception("could not fetch invitees")
            }
            outcome as OperationOutcome.Successful
            invitees.add(outcome.data!!)
        }
        return invitees
    }

    private suspend fun getAppState(): AppState {
        return appStateRepo.get().first()
    }

    private suspend fun getGameState(): GamePrepState? {
        return getAppState().gamePrepState
    }

    private suspend fun getThisUser(): User? {
        return thisUserRepo.get()
    }
}