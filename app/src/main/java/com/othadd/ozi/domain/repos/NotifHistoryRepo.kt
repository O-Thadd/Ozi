package com.othadd.ozi.domain.repos

import com.othadd.ozi.domain.model.NotifHistoryEntry

interface NotifHistoryRepo {

    fun getEntry(chatId: String): NotifHistoryEntry?

    suspend fun updateEntry(notifHistoryEntry: NotifHistoryEntry)

    suspend fun wipeEntries()

    suspend fun addEntry(notifHistoryEntry: NotifHistoryEntry)
}