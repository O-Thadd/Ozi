package com.othadd.ozi.domain.useCases.defaultImplementations.user

import com.othadd.ozi.domain.model.DataSourcePreference
import com.othadd.ozi.domain.model.OperationOutcome
import com.othadd.ozi.domain.model.User
import com.othadd.ozi.domain.repos.ThisUserRepo
import com.othadd.ozi.domain.repos.UsersRepo
import com.othadd.ozi.domain.useCases.interfaces.user.GetUsersUseCases
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUsersUseCasesImpl @Inject constructor(
    private val usersRepo: UsersRepo,
    private val thisUserRepo: ThisUserRepo
) : GetUsersUseCases {

    override suspend fun explore(): OperationOutcome<List<User>, Nothing> {
        val outcome = usersRepo.exploreUsers()
        if (outcome is OperationOutcome.Successful){
            outcome.data?.let {
                val users = removeThisUser(it)
                return OperationOutcome.Successful(users)
            }
        }

        return outcome
    }

    override suspend fun search(searchTerm: String): OperationOutcome<List<User>, Nothing> {
        val outcome = usersRepo.searchUsers(searchTerm)
        if (outcome is OperationOutcome.Successful){
            outcome.data?.let {
                val users = removeThisUser(it)
                return OperationOutcome.Successful(users)
            }
        }

        return outcome
    }

    override suspend fun getUserFlow(userId: String): Flow<User> {
        return usersRepo.getUserFlow(userId)
    }

    override suspend fun getUser(
        userId: String,
        dataSourcePreference: DataSourcePreference
    ): User? {
        val outcome = usersRepo.getUser(userId, dataSourcePreference)
        if (outcome is OperationOutcome.Failed) {
            return null
        }

        outcome as OperationOutcome.Successful
        return outcome.data
    }

    override suspend fun syncUser(userId: String) {
        usersRepo.syncUser(userId)
    }

    private suspend fun removeThisUser(users: List<User>): List<User>{
        val thisUser = thisUserRepo.get()
        thisUser ?: return users

        return users.filter { it.userId != thisUser.userId }
    }
}