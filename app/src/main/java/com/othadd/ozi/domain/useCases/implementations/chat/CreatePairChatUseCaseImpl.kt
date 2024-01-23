package com.othadd.ozi.domain.useCases.implementations.chat

import com.othadd.ozi.domain.model.chat.Chat
import com.othadd.ozi.domain.repos.ChatRepo
import com.othadd.ozi.domain.repos.ThisUserRepo
import com.othadd.ozi.domain.repos.UsersRepo
import com.othadd.ozi.domain.model.User
import com.othadd.ozi.common.derivePairChatId
import com.othadd.ozi.domain.useCases.interfaces.chat.CreatePairChatUseCase
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