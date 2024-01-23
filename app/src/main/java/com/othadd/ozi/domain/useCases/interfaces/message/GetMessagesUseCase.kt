package com.othadd.ozi.domain.useCases.interfaces.message

import com.othadd.ozi.domain.model.message.ChatItem
import kotlinx.coroutines.flow.Flow

interface GetMessagesUseCase {
    suspend operator fun invoke(chatId: String): Flow<List<ChatItem>>
}