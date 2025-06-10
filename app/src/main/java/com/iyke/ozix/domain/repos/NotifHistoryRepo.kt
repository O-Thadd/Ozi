package com.iyke.ozix.domain.repos

import com.iyke.ozix.domain.model.NotifHistoryEntry

interface NotifHistoryRepo {

    fun getEntry(chatId: String): NotifHistoryEntry?

    suspend fun updateEntry(notifHistoryEntry: NotifHistoryEntry)

    suspend fun wipeEntries()

    suspend fun addEntry(notifHistoryEntry: NotifHistoryEntry)
}