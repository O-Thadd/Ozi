package com.iyke.ozix.domain.useCases.interfaces.message

import com.iyke.ozix.domain.model.message.ChatItem
import kotlinx.coroutines.flow.Flow

interface GetMessagesUseCase {
    suspend operator fun invoke(chatId: String): Flow<List<ChatItem>>
}