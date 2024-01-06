package com.othadd.ozi.domain.useCases.chat

import com.othadd.ozi.domain.model.chat.Chat
import com.othadd.ozi.domain.repos.ChatRepo
import com.othadd.ozi.domain.repos.MessageRepo
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class DeleteChatsUseCase @Inject constructor(
    private val chatRepo: ChatRepo,
    private val messageRepo: MessageRepo
) {

    suspend fun deleteEmptyChats(){
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