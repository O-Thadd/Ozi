package com.othadd.ozi.domain.model.chat

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.othadd.ozi.common.GROUP_CHAT_NAME
import com.othadd.ozi.common.getOtherParticipantId
import com.othadd.ozi.domain.model.DataSourcePreference
import com.othadd.ozi.domain.model.OperationOutcome
import com.othadd.ozi.domain.model.User
import com.othadd.ozi.domain.model.message.Message
import com.othadd.ozi.domain.repos.UsersRepo
import com.othadd.ozi.domain.useCases.implementations.DateTimeFormatType
import com.othadd.ozi.domain.useCases.interfaces.GetFormattedDateTimeUseCase

@Entity(tableName = "chats")
data class Chat(
    @PrimaryKey val chatId: String,
    val participantIds: List<String>,
    val hasUnreadMessage: Boolean
)

fun Chat.deriveUpdatedVersionFromDto(dto: ChatDto): Chat {
    return Chat(
        chatId = chatId,
        participantIds = dto.participantsIds,
        hasUnreadMessage = hasUnreadMessage
    )
}

suspend fun Chat.toUiChat(
    thisUserId: String,
    lastMessage: Message?,
    formattedDateTimeGetter: GetFormattedDateTimeUseCase,
    usersRepo: UsersRepo
): UiChat {

    val chatId = this.chatId
    val lastMessageBody = lastMessage?.body ?: ""
    val lastMessageDateTime =
        lastMessage?.let { formattedDateTimeGetter(it.timestamp, DateTimeFormatType.FOR_CHAT_LIST) }
            ?: ""
    val hasUnreadMessage = this.hasUnreadMessage

    val isPairChat = chatId.length > 40
    if (isPairChat) {
        val chatMateId = getOtherParticipantId(chatId, thisUserId)
        val chatMateFetchOutcome = usersRepo.getUser(chatMateId, DataSourcePreference.LOCAL)
        val chatMate = (chatMateFetchOutcome as OperationOutcome.Successful<User?, Nothing>).data!!

        return UiChat.PairChat(
            chatId = chatId,
            chatName = chatMate.username,
            lastMessage = lastMessageBody,
            lastMessageTimestamp = lastMessage?.timestamp ?: 0,
            lastMessageDateTime = lastMessageDateTime,
            aviFg = chatMate.aviFg,
            aviBg = chatMate.aviBg,
            hasUnreadMessage = hasUnreadMessage,
            verified = chatMate.verified
        )
    } else {
        return UiChat.GroupChat(
            chatId = chatId,
            chatName = GROUP_CHAT_NAME,
            lastMessage = lastMessageBody,
            lastMessageTimestamp = lastMessage?.timestamp ?: 0,
            lastMessageDateTime = lastMessageDateTime,
            aviFg = 200,
            aviBg = 200,
            hasUnreadMessage = hasUnreadMessage
        )
    }
}
