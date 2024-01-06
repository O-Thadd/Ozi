package com.othadd.ozi.domain.useCases.message

import com.othadd.ozi.domain.repos.ChatRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MarkMessagesReadUseCase @Inject constructor(private val chatRepo: ChatRepo) {

    suspend operator fun invoke(chatId: String) {
        withContext(Dispatchers.IO) {
            val chat = chatRepo.getChat(chatId)
            chat ?: return@withContext
            val updatedChat = chat.copy(hasUnreadMessage = false)
            chatRepo.updateChat(updatedChat)
        }
    }
}