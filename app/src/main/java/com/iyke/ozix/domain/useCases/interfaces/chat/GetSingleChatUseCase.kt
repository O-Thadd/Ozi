package com.iyke.ozix.domain.useCases.interfaces.chat

import com.iyke.ozix.domain.model.chat.Chat
import kotlinx.coroutines.flow.Flow

interface GetSingleChatUseCase {
    suspend operator fun invoke(chatId: String): Flow<Chat>
}