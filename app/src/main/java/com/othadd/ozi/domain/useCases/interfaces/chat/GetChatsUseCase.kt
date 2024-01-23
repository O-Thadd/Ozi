package com.othadd.ozi.domain.useCases.interfaces.chat

import com.othadd.ozi.domain.model.chat.UiChat
import kotlinx.coroutines.flow.Flow

interface GetChatsUseCase {
    suspend operator fun invoke(): Flow<List<UiChat>>
}