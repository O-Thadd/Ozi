package com.iyke.ozix.data.repos

import com.google.gson.JsonObject
import com.iyke.ozix.common.Params
import com.iyke.ozix.common.stringListToString
import com.iyke.ozix.data.dataSources.localStore.OziDataStore
import com.iyke.ozix.data.dataSources.remote.OziRemoteService
import com.iyke.ozix.domain.model.User
import com.iyke.ozix.domain.model.gaming.GamingCommunicationTarget
import com.iyke.ozix.domain.model.gaming.ResponseToBroker
import com.iyke.ozix.domain.repos.AppStateRepo
import com.iyke.ozix.domain.repos.GamingRepo
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GamingRepoImpl @Inject constructor(
    private val remoteService: OziRemoteService,
    private val dataStore: OziDataStore,
    private val appStateRepo: AppStateRepo
): GamingRepo {
    override suspend fun sendGamingRequest(participantsIds: List<String>): String {
        val thisUser = dataStore.getThisUserFlow().first()!!
        val participantsIdsString = stringListToString(participantsIds)
        val dataJson = JsonObject()
        dataJson.addProperty(Params.GAMING_COMMUNICATION_TARGET.string, GamingCommunicationTarget.SERVICE.string)
        dataJson.addProperty("participantsIds", participantsIdsString)
        dataJson.addProperty("senderId", thisUser.userId)
        return remoteService.postToGaming(dataJson.toString(), thisUser.token!!)
    }

    override suspend fun acceptGamingRequest() {
        val targetResponseSenderIdArgumentPairs = getTargetResponseSenderIdArgumentPairs(
            GamingCommunicationTarget.BROKER.string,
            ResponseToBroker.ACCEPT.string
        )
        val argumentsJson = buildParamsJson(*targetResponseSenderIdArgumentPairs.toTypedArray())

        remoteService.postToGaming(argumentsJson.toString(), getThisUser().token!!)
    }

    override suspend fun declineGamingRequest() {
        val targetResponseSenderIdArgumentPairs = getTargetResponseSenderIdArgumentPairs(
            GamingCommunicationTarget.BROKER.string,
            ResponseToBroker.DECLINE.string
        )
        val argumentsJson = buildParamsJson(*targetResponseSenderIdArgumentPairs.toTypedArray())

        remoteService.postToGaming(argumentsJson.toString(), getThisUser().token!!)
    }

    override suspend fun proceed() {
        val targetResponseSenderIdArgumentPairs = getTargetResponseSenderIdArgumentPairs(
            GamingCommunicationTarget.BROKER.string,
            ResponseToBroker.PROCEED.string
        )
        val argumentsJson = buildParamsJson(*targetResponseSenderIdArgumentPairs.toTypedArray())

        remoteService.postToGaming(argumentsJson.toString(), getThisUser().token!!)
    }

    override suspend fun cancelGame() {
        val targetResponseSenderIdArgumentPairs = getTargetResponseSenderIdArgumentPairs(
            GamingCommunicationTarget.BROKER.string,
            ResponseToBroker.CANCEL.string
        )
        val argumentsJson = buildParamsJson(*targetResponseSenderIdArgumentPairs.toTypedArray())

        remoteService.postToGaming(argumentsJson.toString(), getThisUser().token!!)
    }




    private fun buildParamsJson(vararg arguments: Pair<String, String>): JsonObject {
        val jsonObject = JsonObject()
        for (argument in arguments){
            jsonObject.addProperty(argument.first, argument.second)
        }
        return jsonObject
    }

    private suspend fun getTargetResponseSenderIdArgumentPairs(target: String, response: String): MutableList<Pair<String, String>> {
        val argumentPairs = mutableListOf<Pair<String, String>>()
        val appState = appStateRepo.get().first()
        argumentPairs.add(Pair(Params.GAMING_COMMUNICATION_TARGET.string, target))
        argumentPairs.add(Pair("response", response))
        argumentPairs.add(Pair("senderId", getThisUser().userId))
        appState.gameBrokerId?.let {
            argumentPairs.add(Pair("brokerId", it))
        }
        return argumentPairs
    }

    private suspend fun getThisUser(): User{
        return dataStore.getThisUserFlow().first()!!
    }
}