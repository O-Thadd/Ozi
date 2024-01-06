package com.othadd.ozi.domain.useCases.chat

import com.othadd.ozi.domain.model.chat.Chat
import com.othadd.ozi.domain.repos.ChatRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetChatUseCase @Inject constructor(private val chatRepo: ChatRepo) {

    suspend operator fun invoke(chatId: String): Flow<Chat> {
        return chatRepo.getChatFlow(chatId).map { it!! }
    }

}