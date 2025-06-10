package com.iyke.ozix.domain.useCases.defaultImplementations.chat

import com.iyke.ozix.domain.model.chat.Chat
import com.iyke.ozix.domain.repos.ChatRepo
import com.iyke.ozix.domain.useCases.interfaces.chat.GetSingleChatUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetSingleChatUseCaseImpl @Inject constructor(private val chatRepo: ChatRepo) :
    GetSingleChatUseCase {

    override suspend operator fun invoke(chatId: String): Flow<Chat> {
        return chatRepo.getChatFlow(chatId).map { it!! }
    }

}