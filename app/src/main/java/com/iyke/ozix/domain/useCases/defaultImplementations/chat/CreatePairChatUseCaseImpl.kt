package com.iyke.ozix.domain.useCases.defaultImplementations.chat

import com.iyke.ozix.domain.model.chat.Chat
import com.iyke.ozix.domain.repos.ChatRepo
import com.iyke.ozix.domain.repos.ThisUserRepo
import com.iyke.ozix.domain.repos.UsersRepo
import com.iyke.ozix.domain.model.User
import com.iyke.ozix.common.derivePairChatId
import com.iyke.ozix.domain.useCases.interfaces.chat.CreatePairChatUseCase
import javax.inject.Inject

class CreatePairChatUseCaseImpl @Inject constructor(
    private val thisUserRepo: ThisUserRepo,
    private val chatRepo: ChatRepo,
    private val usersRepo: UsersRepo
) : CreatePairChatUseCase {

    override suspend operator fun invoke(chatMate: User): String {
        val chatId = createChat(chatMate.userId)
        usersRepo.addUser(chatMate)
        return chatId
    }

    private suspend fun createChat(chatMateId: String): String {
        val thisUserId = thisUserRepo.get()!!.userId
        val chatId = derivePairChatId(thisUserId, chatMateId)
        val chat = Chat(
            chatId,
            listOf(thisUserId, chatMateId),
            false
        )
        chatRepo.addChat(chat)
        return chatId
    }
}