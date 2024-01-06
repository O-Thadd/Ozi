package com.othadd.ozi.domain.repos

import com.othadd.ozi.domain.model.OperationOutcome
import com.othadd.ozi.domain.model.Signal

interface SignalRepo {

    suspend fun getSignals(): OperationOutcome<List<Signal>, Nothing>

}