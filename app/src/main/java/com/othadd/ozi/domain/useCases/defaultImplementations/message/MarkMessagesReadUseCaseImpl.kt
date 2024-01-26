package com.othadd.ozi.domain.useCases.defaultImplementations.message

import com.othadd.ozi.domain.repos.ChatRepo
import com.othadd.ozi.domain.useCases.interfaces.message.MarkMessagesReadUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MarkMessagesReadUseCaseImpl @Inject constructor(private val chatRepo: ChatRepo) :
    MarkMessagesReadUseCase {

    override suspend operator fun invoke(chatId: String) {
        withContext(Dispatchers.IO) {
            val chat = chatRepo.getChat(chatId)
            chat ?: return@withContext
            val updatedChat = chat.copy(hasUnreadMessage = false)
            chatRepo.updateChat(updatedChat)
        }
    }
}