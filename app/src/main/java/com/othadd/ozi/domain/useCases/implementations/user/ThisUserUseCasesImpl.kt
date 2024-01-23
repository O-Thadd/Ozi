package com.othadd.ozi.domain.useCases.implementations.user

import com.othadd.ozi.domain.model.OperationOutcome
import com.othadd.ozi.domain.repos.ThisUserRepo
import com.othadd.ozi.domain.model.User
import com.othadd.ozi.domain.useCases.interfaces.user.ThisUserUseCases
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ThisUserUseCasesImpl @Inject constructor(private val thisUserRepo: ThisUserRepo) :
    ThisUserUseCases {

    override fun get(): Flow<User?> {
        return thisUserRepo.getFlow()
    }

    override suspend fun refresh(): OperationOutcome<Nothing, Nothing> {
        return thisUserRepo.refresh()
    }

    override suspend fun updateLocal(user: User?){
        thisUserRepo.updateLocal(user)
    }

    override suspend fun updateRemote(user: User): OperationOutcome<Nothing, Nothing> {
        return thisUserRepo.updateRemote(user)
    }

}