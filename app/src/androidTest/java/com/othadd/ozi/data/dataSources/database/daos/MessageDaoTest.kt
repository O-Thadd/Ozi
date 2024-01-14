package com.othadd.ozi.data.dataSources.database.daos

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.othadd.ozi.data.dataSources.database.OziDatabase
import com.othadd.ozi.domain.model.message.Message
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class MessageDaoTest {

    private lateinit var messageDao: MessageDao
    private lateinit var db: OziDatabase

    private val message1 = Message(
        senderId = "sender1id",
        chatId = "chat1id",
        body = "message1body",
        timestamp = 1L
    )
    private val message2 = Message(
        senderId = "sender2id",
        chatId = "chat2id",
        body = "message2body",
        timestamp = 2L
    )
    private val message3 = Message(
        senderId = "sender3id",
        chatId = "chat3id",
        body = "message3body",
        timestamp = 3L
    )

    private val allMessages = listOf(message1, message2, message3)

    private fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, OziDatabase::class.java).build()
        messageDao = db.messageDao()
    }

    private fun populateDb() = runBlocking {
        messageDao.insert(message1)
        messageDao.insert(message2)
        messageDao.insert(message3)
    }

    @Before
    fun setUp(){
        createDb()
        populateDb()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun messageDao_insert_read() = runTest {
    val testMessage = Message(senderId = "testSenderId", chatId = "testMessageChatId", timestamp = 7L, body = "testMessageBody")
        messageDao.insert(testMessage)
        val retrievedMessages = messageDao.getMessagesByChatId(testMessage.chatId).first()
        assertEquals(retrievedMessages.size, 1)
        assertEquals(retrievedMessages[0], testMessage)
    }

    @Test
    fun messageDao_insertMultiple_read() = runTest {
        val chatId = "multipleMessagesChatId"
        val testMessage1 = Message(senderId = "testSenderId1", chatId = chatId, timestamp = 7L, body = "testMessage1Body")
        val testMessage2 = Message(senderId = "testSenderId2", chatId = chatId, timestamp = 7L, body = "testMessage2Body")
        messageDao.insertMultiple(listOf(testMessage1, testMessage2))
        val retrievedMessages = messageDao.getMessagesByChatId(chatId).first()
        assertEquals(retrievedMessages.size, 2)
        assertEquals(retrievedMessages, listOf(testMessage1, testMessage2))
    }

    @Test
    fun messageDao_getAll_returnsAll() = runTest {
        val retrievedMessages = messageDao.getAllMessages().first()
        assertEquals(retrievedMessages, allMessages)
    }

    @Test
    fun messageDao_deleteAll_returnsEmpty() = runTest {
        messageDao.deleteAll()
        val retrievedMessages = messageDao.getAllMessages().first()
        assertTrue(retrievedMessages.isEmpty())
    }

}