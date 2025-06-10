package com.iyke.ozix.domain.repos

import com.iyke.ozix.domain.model.OperationOutcome
import com.iyke.ozix.domain.model.Signal

interface SignalRepo {

    suspend fun getSignals(): OperationOutcome<List<Signal>, Nothing>

}