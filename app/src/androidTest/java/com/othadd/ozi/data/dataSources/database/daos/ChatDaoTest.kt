package com.othadd.ozi.data.dataSources.database.daos

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.othadd.ozi.data.dataSources.database.OziDatabase
import com.othadd.ozi.domain.model.chat.Chat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class ChatDaoTest {

    private lateinit var chatDao: ChatDao
    private lateinit var db: OziDatabase

    private val chat1 = Chat("chat1", listOf("id1, id2"), false)
    private val chat2 = Chat("chat2", listOf("id1, id2"), false)
    private val chat3 = Chat("chat3", listOf("id1, id2"), false)
    private val chat4 = Chat("chat4", listOf("id1, id2"), false)

    private fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, OziDatabase::class.java).build()
        chatDao = db.chatDao()
    }

    private fun populateDb() = runBlocking {
        chatDao.insert(chat1)
        chatDao.insert(chat2)
        chatDao.insert(chat3)
        chatDao.insert(chat4)
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
    fun chatDao_insert_read() = runTest {
        val testChatId = "testChat"
        val testChat = Chat(testChatId, listOf("testParticipantId"), false)
        chatDao.insert(testChat)
        val retrievedChat = chatDao.getChatByChatId(testChatId)
        assertEquals(testChat, retrievedChat)
    }

    @Test
    fun chatDao_delete_deleted() = runTest {
        chatDao.delete(listOf(chat1))
        val retrievedChat = chatDao.getChatByChatId(chat1.chatId)
        assertNull(retrievedChat)
    }

    @Test
    fun chatDao_update_updated() = runTest {
        val updatedParticipantIds = listOf("updatedParticipantId")
        val updatedChat = Chat(chat1.chatId, updatedParticipantIds, false)
        chatDao.update(updatedChat)
        val retrievedChat = chatDao.getChatByChatId(chat1.chatId)
        assertEquals(updatedParticipantIds, retrievedChat?.participantIds)
    }

    @Test
    fun chatDao_getChats_returnAllChats() = runTest {
        val retrievedChats = chatDao.getChats().first()
        assertEquals(listOf(chat1, chat2, chat3, chat4), retrievedChats)
    }
}