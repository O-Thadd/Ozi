package com.iyke.ozix.domain.useCases.defaultImplementations.message

import com.iyke.ozix.common.GAME_MODERATOR_SENDER_ID
import com.iyke.ozix.domain.repos.MessageRepo
import com.iyke.ozix.domain.repos.ThisUserRepo
import com.iyke.ozix.domain.model.message.ChatItem
import com.iyke.ozix.common.groupByDate
import com.iyke.ozix.common.toChatItems
import com.iyke.ozix.domain.model.DataSourcePreference
import com.iyke.ozix.domain.model.OperationOutcome
import com.iyke.ozix.domain.repos.UsersRepo
import com.iyke.ozix.domain.useCases.interfaces.message.GetMessagesUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetMessagesUseCaseImpl @Inject constructor(
    private val messageRepo: MessageRepo,
    private val thisUserRepo: ThisUserRepo,
    private val usersRepo: UsersRepo
) : GetMessagesUseCase {
    override suspend operator fun invoke(chatId: String): Flow<List<ChatItem>> {
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