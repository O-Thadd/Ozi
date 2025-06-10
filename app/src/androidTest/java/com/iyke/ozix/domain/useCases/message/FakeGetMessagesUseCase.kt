package com.iyke.ozix.domain.useCases.message

import com.iyke.ozix.domain.model.message.ChatItem
import com.iyke.ozix.domain.useCases.interfaces.message.GetMessagesUseCase
import com.iyke.ozix.testChatItems
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeGetMessagesUseCase: GetMessagesUseCase {
    override suspend fun invoke(chatId: String): Flow<List<ChatItem>> {
        return flowOf(testChatItems)
    }
}