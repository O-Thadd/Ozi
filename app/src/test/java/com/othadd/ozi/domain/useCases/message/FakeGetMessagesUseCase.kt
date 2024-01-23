package com.othadd.ozi.domain.useCases.message

import com.othadd.ozi.domain.model.message.ChatItem
import com.othadd.ozi.domain.useCases.interfaces.message.GetMessagesUseCase
import com.othadd.ozi.testChatItems
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeGetMessagesUseCase: GetMessagesUseCase {
    override suspend fun invoke(chatId: String): Flow<List<ChatItem>> {
        return flowOf(testChatItems)
    }
}