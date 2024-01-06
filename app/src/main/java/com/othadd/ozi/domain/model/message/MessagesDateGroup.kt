package com.othadd.ozi.domain.model.message

/**
 * Represents a group of messages sent between users on a particular date.
 *
 * @property datetime Day and date during which messages in this group were sent. With the format: "EEEE, dd.MM.yyyy". For example: Thursday, 13.07.2023
 * @property messages Messages in this group
 * @property T Type of [messages], could be [Message], [UIMessage], and so on.
 */
data class MessagesDateGroup<T>(val datetime: String, val messages: List<T>)