package com.othadd.ozi.data.dataSources.database.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.othadd.ozi.domain.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)

    @Delete
    suspend fun delete(user: User)

    @Query("DELETE from users")
    fun deleteAll()

    @Update
    suspend fun update(user: User)

    @Query("SELECT * from users")
    fun getUsers(): List<User>

    @Query("SELECT * from users WHERE userId = :userId")
    fun getUserFlowById(userId: String): Flow<User?>

    @Query("SELECT * from users WHERE userId = :userId")
    fun getUserById(userId: String): User?

}