package com.iyke.ozix.domain.useCases.defaultImplementations.user

import com.iyke.ozix.data.dataSources.model.OpError
import com.iyke.ozix.domain.model.DataSourcePreference
import com.iyke.ozix.domain.model.OperationOutcome
import com.iyke.ozix.domain.model.OperationOutcomeX
import com.iyke.ozix.domain.model.User
import com.iyke.ozix.domain.repos.ThisUserRepo
import com.iyke.ozix.domain.repos.UsersRepo
import com.iyke.ozix.domain.useCases.interfaces.user.GetUsersUseCases
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUsersUseCasesImpl @Inject constructor(
    private val usersRepo: UsersRepo,
    private val thisUserRepo: ThisUserRepo
) : GetUsersUseCases {

    override suspend fun explore(): OperationOutcomeX<List<User>, OpError> {
        val outcome = usersRepo.exploreUsers()
        if (outcome is OperationOutcomeX.Successful){
            outcome.data?.let {
                val users = removeThisUser(it)
                return OperationOutcomeX.Successful(users)
            }
        }

        return outcome
    }

    override suspend fun search(searchTerm: String): OperationOutcomeX<List<User>, OpError> {
        val outcome = usersRepo.searchUsers(searchTerm)
        if (outcome is OperationOutcomeX.Successful){
            outcome.data?.let {
                val users = removeThisUser(it)
                return OperationOutcomeX.Successful(users)
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