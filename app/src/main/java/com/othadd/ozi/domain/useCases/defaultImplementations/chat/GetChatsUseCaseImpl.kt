package com.othadd.ozi.domain.useCases.defaultImplementations.chat

import com.othadd.ozi.domain.model.chat.UiChat
import com.othadd.ozi.domain.model.chat.toUiChat
import com.othadd.ozi.domain.repos.ChatRepo
import com.othadd.ozi.domain.repos.MessageRepo
import com.othadd.ozi.domain.repos.ThisUserRepo
import com.othadd.ozi.domain.repos.UsersRepo
import com.othadd.ozi.domain.useCases.defaultImplementations.GetFormattedDateTimeUseCaseImpl
import com.othadd.ozi.domain.useCases.interfaces.chat.GetChatsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetChatsUseCaseImpl @Inject constructor(
    private val chatRepo: ChatRepo,
    private val thisUserRepo: ThisUserRepo,
    private val messageRepo: MessageRepo,
    private val usersRepo: UsersRepo
) : GetChatsUseCase {

    override suspend operator fun invoke(): Flow<List<UiChat>> {
        val thisUserFlow = thisUserRepo.getFlow()
        val messagesFlow = messageRepo.getAllMessages()
        val chatsFlow = chatRepo.getChats()

        return combine(messagesFlow, chatsFlow, thisUserFlow) { messages, chats, thisUser ->
            thisUser ?: return@combine emptyList<UiChat>()
            chats.map { chat ->
                val chatMessages = messages.filter { it.chatId == chat.chatId }
                val lastMessage = chatMessages.maxByOrNull { it.timestamp }
                chat.toUiChat(thisUser.userId, lastMessage, GetFormattedDateTimeUseCaseImpl(), usersRepo)
            }.sortedByDescending { it.lastMessageTimestamp }
        }
    }
}