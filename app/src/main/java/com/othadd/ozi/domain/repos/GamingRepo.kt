package com.othadd.ozi.domain.repos

interface GamingRepo {

    suspend fun sendGamingRequest(participantsIds: List<String>): String

    suspend fun acceptGamingRequest()

    suspend fun declineGamingRequest()

    suspend fun proceed()

    suspend fun cancelGame()
}