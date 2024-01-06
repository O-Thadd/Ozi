package com.othadd.ozi.common

import android.icu.util.Calendar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.othadd.ozi.domain.model.User
import com.othadd.ozi.domain.model.message.Message
import com.othadd.ozi.domain.model.message.toUIMessage
import com.othadd.ozi.domain.model.message.ChatItem
import com.othadd.ozi.domain.model.message.ChatItemType
import com.othadd.ozi.domain.model.message.MessagesDateGroup
import com.othadd.ozi.domain.model.message.TimeStamp
import java.lang.reflect.Type
import java.text.SimpleDateFormat

fun messageToString(message: Message): String {
    val gson = Gson()
    return gson.toJson(message)
}

fun stringToMessage(jsonString: String): Message {
    val gson = Gson()
    val collectionType: Type = object : TypeToken<Message?>() {}.type
    return gson.fromJson(jsonString, collectionType)
}

fun stringListToString(strings: List<String>): String{
    val gson = Gson()
    return gson.toJson(strings)
}

/**
 * Will return an empty list if provided string is empty or consists solely of white spaces
 */
fun stringToStringList(jsonString: String): List<String> {
    if (jsonString.isBlank()){
        return emptyList()
    }

    val gson = Gson()
    val collectionType: Type = object : TypeToken<List<String?>?>() {}.type
    return gson.fromJson(jsonString, collectionType)
}

/**
 * Groups messages by date into [MessagesDateGroup]s.
 * This function expects to be called on an already sorted list.
 */
fun List<Message>.groupByDate(): List<MessagesDateGroup<Message>>{
    if (this.isEmpty()){
        return emptyList()
    }

    val groups = mutableListOf<MessagesDateGroup<Message>>()

    val group = mutableListOf<Message>()

    var groupCalendar = Calendar.getInstance().apply { timeInMillis = this@groupByDate[0].timestamp }
    var groupDate = with(groupCalendar){ Triple(get(Calendar.DATE), get(Calendar.MONTH), get(Calendar.YEAR)) }

    for ((index, message) in this.withIndex()){
        group.add(message)

        if (index == this.lastIndex){
            concludeGroup(groupCalendar, groups, group)
            break
        }

        val nextMessage = this[index + 1]
        val nextMessageCalendar = Calendar.getInstance().apply { timeInMillis = nextMessage.timestamp }
        val nextMessageDate = with(nextMessageCalendar) { Triple(get(Calendar.DATE), get(Calendar.MONTH), get(Calendar.YEAR)) }
        if (groupDate != nextMessageDate){
            concludeGroup(groupCalendar, groups, group)
        }

        groupCalendar = nextMessageCalendar
        groupDate = nextMessageDate
    }

    return groups
}

private fun concludeGroup(
    groupCalendar: Calendar,
    groups: MutableList<MessagesDateGroup<Message>>,
    group: MutableList<Message>
) {
    val format = SimpleDateFormat("EEEE, dd.MM.yyyy")
    val groupDateObj = groupCalendar.time
    val uiDate = format.format(groupDateObj)
    groups.add(MessagesDateGroup(uiDate, group.toList()))
    group.clear()
}

/**
 * Returns a list of [ChatItem]s. Also adding [ChatItem]s representing timestamps and dates where necessary.
 */
suspend fun List<MessagesDateGroup<Message>>.toChatItems(thisUserId: String, getUser: suspend (String) -> User?): List<ChatItem>{
    val chatItems = mutableListOf<ChatItem>()
    var showSenderNameForNextMessage = true

    for (group in this) {
        chatItems.add(ChatItem(group.datetime, ChatItemType.DATE))
        for ((index, message) in group.messages.withIndex()) {
            val user = if (showSenderNameForNextMessage) getUser(message.senderId) else null
            val uiMessage = message.toUIMessage(thisUserId, user)
            chatItems.add(uiMessage)
            if (index == group.messages.lastIndex) {
                chatItems.add(TimeStamp(uiMessage.time, uiMessage.sent, uiMessage.senderType))
                showSenderNameForNextMessage = true
                continue
            }

            val nextMessage = group.messages[index + 1]
            val timeIntervalBetweenThisMessageAndNext =
                (nextMessage.timestamp - message.timestamp) / 1000 // in seconds
            val sameSenderForThisMessageAndNext = message.senderId == nextMessage.senderId
            showSenderNameForNextMessage =
                if (timeIntervalBetweenThisMessageAndNext > 60 || !sameSenderForThisMessageAndNext) {
                    chatItems.add(TimeStamp(uiMessage.time, uiMessage.sent, uiMessage.senderType))
                    true
                } else {
                    false
                }
        }
    }

    return chatItems
}

fun derivePairChatId(thisUserId: String, chatMateId: String): String {
    return "${minOf(thisUserId, chatMateId)}${maxOf(thisUserId, chatMateId)}"
}

fun getOtherParticipantId(chatId: String, knownParticipantId: String): String {
    val chatIdLength = chatId.length
    val string1 = chatId.substring(0, chatIdLength / 2)
    val string2 = chatId.substring(chatIdLength / 2)
    return if (knownParticipantId == string1) {
        string2
    } else {
        string1
    }
}
