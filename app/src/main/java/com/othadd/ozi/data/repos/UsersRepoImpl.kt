package com.othadd.ozi.data.repos

import com.othadd.ozi.data.dataSources.database.daos.UserDao
import com.othadd.ozi.data.dataSources.model.RemoteUsersRequestType
import com.othadd.ozi.data.dataSources.remote.OziRemoteService
import com.othadd.ozi.domain.model.DataSourcePreference
import com.othadd.ozi.domain.repos.UsersRepo
import com.othadd.ozi.domain.model.OperationOutcome
import com.othadd.ozi.domain.model.User
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
                return try {
                    val result = remoteService.getUsers(
                        requestType = RemoteUsersRequestType.SPECIFIC.string,
                        userId = userId
                    )
                    OperationOutcome.Successful(result[0])
                } catch (e: Exception) {
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
        val fetchedUser = try {
            remoteService.getUsers(
                requestType = RemoteUsersRequestType.SPECIFIC.string,
                userId = userId
            )[0]
        } catch (e: Exception) {
            return
        }

        if (localUser == null) {
            userDao.insert(fetchedUser)
        } else {
            userDao.update(fetchedUser)
        }
    }

    override suspend fun exploreUsers(): OperationOutcome<List<User>, Nothing> {
        return try {
            val response =
                remoteService.getUsers(requestType = RemoteUsersRequestType.SUGGESTED.string)
            OperationOutcome.Successful(response)
        } catch (e: Exception) {
            OperationOutcome.Failed()
        }
    }

    override suspend fun searchUsers(searchTerm: String): OperationOutcome<List<User>, Nothing> {
        return try {
            val response = remoteService.getUsers(
                requestType = RemoteUsersRequestType.SEARCH.string,
                username = searchTerm
            )
            OperationOutcome.Successful(response)
        } catch (e: Exception) {
            OperationOutcome.Failed(null)
        }
    }

    override suspend fun deleteAll() {
        userDao.deleteAll()
    }
}
