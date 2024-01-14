package com.othadd.ozi.data.repos

import com.google.gson.JsonObject
import com.othadd.ozi.common.Params
import com.othadd.ozi.common.stringListToString
import com.othadd.ozi.data.dataSources.localStore.DefaultOziDataStore
import com.othadd.ozi.data.dataSources.localStore.OziDataStore
import com.othadd.ozi.data.dataSources.remote.OziRemoteService
import com.othadd.ozi.domain.model.User
import com.othadd.ozi.domain.model.gaming.GamingCommunicationTarget
import com.othadd.ozi.domain.model.gaming.ResponseToBroker
import com.othadd.ozi.domain.repos.AppStateRepo
import com.othadd.ozi.domain.repos.GamingRepo
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