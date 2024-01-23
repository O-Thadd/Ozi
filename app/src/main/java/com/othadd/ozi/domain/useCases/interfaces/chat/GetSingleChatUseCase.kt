package com.othadd.ozi.domain.useCases.interfaces.chat

import com.othadd.ozi.domain.model.chat.Chat
import kotlinx.coroutines.flow.Flow

interface GetSingleChatUseCase {
    suspend operator fun invoke(chatId: String): Flow<Chat>
}