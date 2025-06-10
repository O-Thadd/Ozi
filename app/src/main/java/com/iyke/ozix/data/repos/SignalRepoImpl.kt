package com.iyke.ozix.data.repos

import com.iyke.ozix.data.dataSources.localStore.OziDataStore
import com.iyke.ozix.data.dataSources.remote.OziRemoteService
import com.iyke.ozix.domain.repos.SignalRepo
import com.iyke.ozix.domain.model.OperationOutcome
import com.iyke.ozix.domain.model.Signal
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