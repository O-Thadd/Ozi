package com.othadd.ozi.data.repos

import com.othadd.ozi.data.dataSources.FakeChatDao
import com.othadd.ozi.data.dataSources.FakeOziDataStore
import com.othadd.ozi.data.dataSources.FakeOziRemoteService
import com.othadd.ozi.testUser1
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ChatRepoImplTest {

    private lateinit var chatRepoImpl: ChatRepoImpl

    @Before
    fun setUp() = runBlocking {
        val fakeDataStore = FakeOziDataStore().apply { updateThisUser(testUser1) }
        chatRepoImpl = ChatRepoImpl(FakeChatDao(), FakeOziRemoteService(), fakeDataStore)
    }

    @Test
    fun chatRepoImpl_syncChat_returnNewChat() = runTest {
        var retrievedChat = chatRepoImpl.getChat("chat1")
        assertNull(retrievedChat)

        chatRepoImpl.syncChat("chat1")

        retrievedChat = chatRepoImpl.getChat("chat1")
        assertNotNull(retrievedChat)
    }
}