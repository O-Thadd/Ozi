package com.iyke.ozix.domain.useCases.defaultImplementations.chat

import com.iyke.ozix.domain.model.chat.Chat
import com.iyke.ozix.domain.repos.ChatRepo
import com.iyke.ozix.domain.repos.MessageRepo
import com.iyke.ozix.domain.useCases.interfaces.chat.DeleteChatsUseCase
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class DeleteChatsUseCaseImpl @Inject constructor(
    private val chatRepo: ChatRepo,
    private val messageRepo: MessageRepo
) : DeleteChatsUseCase {

    override suspend fun deleteEmptyChats(){
        val chatsToDelete = mutableListOf<Chat>()
        val allChats = chatRepo.getChats().first()
        val allMessages = messageRepo.getAllMessages().first()
        for (chat in allChats){
            if (allMessages.none { it.chatId == chat.chatId }){
                chatsToDelete.add(chat)
            }
        }
        chatRepo.deleteChats(chatsToDelete)
    }
}