package com.othadd.ozi.domain.useCases.message

import com.google.gson.JsonObject
import com.othadd.ozi.common.JsonResponseField
import com.othadd.ozi.common.Params
import com.othadd.ozi.domain.model.gaming.GamingCommunicationTarget
import com.othadd.ozi.domain.repos.MessageRepo
import com.othadd.ozi.domain.repos.ThisUserRepo
import com.othadd.ozi.domain.model.message.Message
import com.othadd.ozi.domain.repos.AppStateRepo
import kotlinx.coroutines.flow.first
import java.util.Calendar
import javax.inject.Inject

/**
 * Returns the [Message] object sent
 */
class SendMessageUseCase @Inject constructor(
    private val messageRepo: MessageRepo,
    private val thisUserRepo: ThisUserRepo,
    private val appStateRepo: AppStateRepo
) {

    suspend operator fun invoke(messageBody: String, chatId: String): Message {

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