package com.iyke.ozix.domain.useCases.interfaces.chat

import com.iyke.ozix.domain.model.chat.UiChat
import kotlinx.coroutines.flow.Flow

interface GetChatsUseCase {
    suspend operator fun invoke(): Flow<List<UiChat>>
}