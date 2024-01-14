package com.othadd.ozi.data.repos

import com.othadd.ozi.data.dataSources.localStore.DefaultOziDataStore
import com.othadd.ozi.data.dataSources.localStore.OziDataStore
import com.othadd.ozi.data.dataSources.remote.OziRemoteService
import com.othadd.ozi.domain.repos.SignalRepo
import com.othadd.ozi.domain.model.OperationOutcome
import com.othadd.ozi.domain.model.Signal
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class SignalRepoImpl @Inject constructor(
    private val remoteService: OziRemoteService,
    private val dataStore: OziDataStore
) : SignalRepo {
    override suspend fun getSignals(): OperationOutcome<List<Signal>, Nothing> {

        return try {
            val token = dataStore.getThisUserFlow().first()!!.token!!
            val signals = remoteService.getSignals(token)
            OperationOutcome.Successful(signals)
        }
        catch (e: Exception){
            OperationOutcome.Failed()
        }
    }
}