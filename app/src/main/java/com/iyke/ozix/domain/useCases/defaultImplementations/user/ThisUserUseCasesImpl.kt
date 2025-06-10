package com.iyke.ozix.domain.useCases.defaultImplementations.user

import com.iyke.ozix.data.dataSources.model.OpError
import com.iyke.ozix.domain.model.OperationOutcome
import com.iyke.ozix.domain.model.OperationOutcomeX
import com.iyke.ozix.domain.repos.ThisUserRepo
import com.iyke.ozix.domain.model.User
import com.iyke.ozix.domain.useCases.interfaces.user.ThisUserUseCases
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ThisUserUseCasesImpl @Inject constructor(private val thisUserRepo: ThisUserRepo) :
    ThisUserUseCases {

    override fun get(): Flow<User?> {
        return thisUserRepo.getFlow()
    }

    override suspend fun refresh(): OperationOutcomeX<Nothing, OpError> {
        return thisUserRepo.refresh()
    }

    override suspend fun updateLocal(user: User?){
        thisUserRepo.updateLocal(user)
    }

    override suspend fun updateRemote(user: User): OperationOutcome<Nothing, Nothing> {
        return thisUserRepo.updateRemote(user)
    }

}