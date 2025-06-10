package com.iyke.ozix.domain.useCases.defaultImplementations.message

import com.google.gson.JsonObject
import com.iyke.ozix.common.JsonResponseField
import com.iyke.ozix.common.Params
import com.iyke.ozix.domain.model.gaming.GamingCommunicationTarget
import com.iyke.ozix.domain.repos.MessageRepo
import com.iyke.ozix.domain.repos.ThisUserRepo
import com.iyke.ozix.domain.model.message.Message
import com.iyke.ozix.domain.repos.AppStateRepo
import com.iyke.ozix.domain.useCases.interfaces.message.SendMessageUseCase
import kotlinx.coroutines.flow.first
import java.util.Calendar
import javax.inject.Inject

/**
 * Returns the [Message] object sent
 */
class SendMessageUseCaseImpl @Inject constructor(
    private val messageRepo: MessageRepo,
    private val thisUserRepo: ThisUserRepo,
    private val appStateRepo: AppStateRepo
) : SendMessageUseCase {

    override suspend operator fun invoke(messageBody: String, chatId: String): Message {

        val appState = appStateRepo.get().first()
        var meta = ""
        appState.gameModeratorId?.let {
            val metaJson = JsonObject()
            metaJson.addProperty(Params.GAMING_COMMUNICATION_TARGET.string, GamingCommunicationTarget.MODERATOR.string)
            metaJson.addProperty(JsonResponseField.GAME_MODERATOR_ID.string, it)
            meta = metaJson.toString()
        }

        val newMessage =
            Message(
                senderId = thisUserRepo.get()!!.userId,
                chatId = chatId,
                body = messageBody,
                timestamp = Calendar.getInstance().timeInMillis,
                sent = false,
                meta = meta
            )

        messageRepo.sendMessage(newMessage)
        return newMessage
    }
}