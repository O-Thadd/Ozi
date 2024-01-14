package com.othadd.ozi.data.dataSources.database.daos

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.othadd.ozi.data.dataSources.database.OziDatabase
import com.othadd.ozi.testUser1
import com.othadd.ozi.testUser2
import com.othadd.ozi.testUser3
import com.othadd.ozi.testUser4
import com.othadd.ozi.testUser5
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class UsersDaoTest {

    private lateinit var userDao: UserDao
    private lateinit var db: OziDatabase

    private fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, OziDatabase::class.java).build()
        userDao = db.userDao()
    }

    private fun populateDb() = runBlocking {
        userDao.insert(testUser1)
        userDao.insert(testUser2)
        userDao.insert(testUser3)
        userDao.insert(testUser4)
    }

    @Before
    fun setUp() {
        createDb()
        populateDb()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun userDao_insert_read() = runTest {
        userDao.insert(testUser5)
        val retrievedUser = userDao.getUserById(testUser5.userId)
        assertEquals(testUser5, retrievedUser)
    }

    @Test
    fun userDao_delete_deleted() = runTest {
        userDao.delete(testUser1)
        val retrievedUser = userDao.getUserById(testUser1.userId)
        assertNull(retrievedUser)
    }

    @Test
    fun userDao_deleteAll_returnsEmpty() = runTest {
        userDao.deleteAll()
        val retrievedUsers = userDao.getUsers()
        assertTrue(retrievedUsers.isEmpty())
    }
}