package com.othadd.ozi.domain.useCases.defaultImplementations.chat

import com.othadd.ozi.domain.model.chat.Chat
import com.othadd.ozi.domain.repos.ChatRepo
import com.othadd.ozi.domain.useCases.interfaces.chat.GetSingleChatUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetSingleChatUseCaseImpl @Inject constructor(private val chatRepo: ChatRepo) :
    GetSingleChatUseCase {

    override suspend operator fun invoke(chatId: String): Flow<Chat> {
        return chatRepo.getChatFlow(chatId).map { it!! }
    }

}