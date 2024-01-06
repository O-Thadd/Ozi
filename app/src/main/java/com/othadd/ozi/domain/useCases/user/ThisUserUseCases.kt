package com.othadd.ozi.domain.useCases.user

import com.othadd.ozi.domain.model.OperationOutcome
import com.othadd.ozi.domain.repos.ThisUserRepo
import com.othadd.ozi.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class ThisUserUseCases @Inject constructor(private val thisUserRepo: ThisUserRepo) {

    fun get(): Flow<User?> {
        return thisUserRepo.getFlow()
    }

    suspend fun refresh(): OperationOutcome<Nothing, Nothing> {
        return thisUserRepo.refresh()
    }

    suspend fun updateLocal(user: User?){
        thisUserRepo.updateLocal(user)
    }

    suspend fun updateRemote(user: User): OperationOutcome<Nothing, Nothing> {
        return thisUserRepo.updateRemote(user)
    }

}