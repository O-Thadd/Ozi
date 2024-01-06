package com.othadd.ozi.domain.useCases.message

import com.othadd.ozi.common.GAME_MODERATOR_SENDER_ID
import com.othadd.ozi.domain.repos.MessageRepo
import com.othadd.ozi.domain.repos.ThisUserRepo
import com.othadd.ozi.domain.model.message.ChatItem
import com.othadd.ozi.common.groupByDate
import com.othadd.ozi.common.toChatItems
import com.othadd.ozi.domain.model.DataSourcePreference
import com.othadd.ozi.domain.model.OperationOutcome
import com.othadd.ozi.domain.repos.UsersRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetMessagesUseCase @Inject constructor(
    private val messageRepo: MessageRepo,
    private val thisUserRepo: ThisUserRepo,
    private val usersRepo: UsersRepo
) {
    suspend operator fun invoke(chatId: String): Flow<List<ChatItem>> {
        val messagesFlow = messageRepo.getMessages(chatId)
        val thisUserId = thisUserRepo.get()!!.userId
        return messagesFlow.map { messages ->
            messages.sortedBy { it.timestamp }.groupByDate().toChatItems(thisUserId){
                if (it == GAME_MODERATOR_SENDER_ID){
                    return@toChatItems null
                }

                val outcome = usersRepo.getUser(it, DataSourcePreference.LOCAL_FIRST)
                if (outcome is OperationOutcome.Failed){
                    return@toChatItems null
                }

                outcome as OperationOutcome.Successful
                return@toChatItems outcome.data
            }
        }
    }
}