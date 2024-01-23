package com.othadd.ozi.domain.useCases.interfaces.chat

interface DeleteChatsUseCase {
    suspend fun deleteEmptyChats()
}