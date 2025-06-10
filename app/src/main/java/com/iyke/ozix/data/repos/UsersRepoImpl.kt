package com.iyke.ozix.data.repos

import com.iyke.ozix.data.dataSources.database.daos.UserDao
import com.iyke.ozix.data.dataSources.model.ApiCall
import com.iyke.ozix.data.dataSources.model.OpError
import com.iyke.ozix.data.dataSources.remote.OziRemoteService
import com.iyke.ozix.domain.model.DataSourcePreference
import com.iyke.ozix.domain.model.FromApiUser
import com.iyke.ozix.domain.repos.UsersRepo
import com.iyke.ozix.domain.model.OperationOutcome
import com.iyke.ozix.domain.model.OperationOutcomeX
import com.iyke.ozix.domain.model.User
import com.iyke.ozix.domain.model.toUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UsersRepoImpl @Inject constructor(
    private val userDao: UserDao,
    private val remoteService: OziRemoteService
) : UsersRepo {

    override suspend fun getUser(
        userId: String,
        sourcePreference: DataSourcePreference
    ): OperationOutcome<User?, Nothing> {
        when (sourcePreference) {
            DataSourcePreference.REMOTE -> {

                val opOutcome = ApiCall<FromApiUser, User, OpError> {
                    action { remoteService.getUser(userId) }
                    success { OperationOutcomeX.Successful(it.toUser()) }
                    failure { OperationOutcomeX.Failed() }
                }

                return if (opOutcome is OperationOutcomeX.Successful) {
                    OperationOutcome.Successful(opOutcome.data!!)
                } else {
                    OperationOutcome.Failed()
                }
            }

            DataSourcePreference.LOCAL -> {
                return withContext(Dispatchers.IO){
                    val user = userDao.getUserById(userId)
                    OperationOutcome.Successful(user)
                }
            }

            DataSourcePreference.LOCAL_FIRST -> {
                val localOutcome = getUser(userId, DataSourcePreference.LOCAL)
                if (localOutcome is OperationOutcome.Successful){
                    if (localOutcome.data != null){
                        return localOutcome
                    }
                }

                syncUser(userId)
                return getUser(userId, DataSourcePreference.LOCAL)
            }
        }
    }

    override suspend fun getUserFlow(userId: String): Flow<User> {
        return userDao.getUserFlowById(userId).map { it!! }
    }

    override suspend fun addUser(user: User) {
        userDao.insert(user)
    }

    override suspend fun syncUser(userId: String) {
        val localUser = userDao.getUserFlowById(userId).first()

        val opOutcome = ApiCall(
            action = { remoteService.getUser(userId) },
            onSuccess = { OperationOutcomeX.Successful(it) },
            onFailure = { OperationOutcomeX.Failed() }
        ).call()

        if (opOutcome is OperationOutcomeX.Successful) {
            opOutcome.data?.let {
                if (localUser == null) {
                    userDao.insert(it.toUser())
                } else {
                    userDao.update(it.toUser())
                }
            }
        }
    }

    override suspend fun exploreUsers(): OperationOutcomeX<List<User>, OpError> {
        return ApiCall(
            action = { remoteService.getAllUsers() },
            onSuccess = { apiUsers -> OperationOutcomeX.Successful(apiUsers.map { it.toUser() }) },
            onFailure = { OperationOutcomeX.Failed() }
        ).call()
    }

    override suspend fun searchUsers(searchTerm: String): OperationOutcomeX<List<User>, OpError> {
        return ApiCall(
            action = { remoteService.searchUsers(username = searchTerm) },
            onSuccess = { apiUsers -> OperationOutcomeX.Successful(apiUsers.map { it.toUser() }) },
            onFailure = { OperationOutcomeX.Failed() }
        ).call()
    }

    override suspend fun deleteAll() {
        userDao.deleteAll()
    }
}
