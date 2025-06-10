package com.iyke.ozix.domain.useCases.interfaces.chat

interface DeleteChatsUseCase {
    suspend fun deleteEmptyChats()
}