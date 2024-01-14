package com.othadd.ozi.data.dataSources

import com.othadd.ozi.data.dataSources.database.daos.UserDao
import com.othadd.ozi.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf

class FakeUserDao: UserDao {
    private val users = HashMap<String, User>()
    private val usersFlow = MutableStateFlow(users.values.toList())

    override suspend fun insert(user: User) {
        users[user.userId] = user
        usersFlow.value = users.values.toList()
    }

    override suspend fun delete(user: User) {
        users.remove(user.userId)
        usersFlow.value = users.values.toList()
    }

    override fun deleteAll() {
        users.clear()
        usersFlow.value = users.values.toList()
    }

    override suspend fun update(user: User) {
        users[user.userId] = user
        usersFlow.value = users.values.toList()
    }

    override fun getUsers(): List<User> {
        return users.values.toList()
    }

    override fun getUserFlowById(userId: String): Flow<User?> {
        return flowOf(users[userId])
    }

    override fun getUserById(userId: String): User? {
        return users[userId]
    }
}