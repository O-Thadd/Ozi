package com.othadd.ozi.domain.model.message

open class ChatItem(
    open val body: String,
    open val type: ChatItemType
)

data class TimeStamp(
    override val body: String,
    val sent: Boolean,
    val sender: MessageSender,
    override val type: ChatItemType = ChatItemType.TIMESTAMP
): ChatItem(body, type)

enum class ChatItemType{
    MESSAGE,
    DATE,
    TIMESTAMP
}
