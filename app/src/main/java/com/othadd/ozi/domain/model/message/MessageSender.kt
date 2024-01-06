package com.othadd.ozi.domain.model.message

import com.othadd.ozi.common.GAME_MODERATOR_SENDER_ID

/**
 * With regards to the [id] field,
 * the values of [SELF] and [CHATMATE] should never actually be checked.
 * They are in fact empty strings.
 * To get ids, use the actual Ids of this user or the chatmate in the context.
 * The id values of [SYSTEM] and [GAME_MODERATOR] can be used.
 * The values are the actual values of the senderId field in messages supposedly coming from system or game moderator respectively.
 */
enum class MessageSender(val id: String) {
    SELF(""),
    CHATMATE(""),
    SYSTEM("id for system as message sender"),
    GAME_MODERATOR(GAME_MODERATOR_SENDER_ID)

}