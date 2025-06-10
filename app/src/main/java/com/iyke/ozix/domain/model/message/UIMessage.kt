package com.iyke.ozix.domain.model.message

import com.iyke.ozix.domain.model.User

data class UIMessage(
    val id: String,
    val sender: User?,
    val senderType: MessageSender,
    override val body: String,
    val time: String,
    var sent: Boolean,
    override val type: ChatItemType = ChatItemType.MESSAGE
): ChatItem(body, type)