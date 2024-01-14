package com.othadd.ozi.data.dataSources

import com.othadd.ozi.data.dataSources.database.daos.NotifHistoryDao
import com.othadd.ozi.domain.model.NotifHistoryEntry

class FakeNotifHistoryDao: NotifHistoryDao {
    private val entries = mutableListOf<NotifHistoryEntry>()


    override suspend fun insert(notifHistory: NotifHistoryEntry) {
        entries.add(notifHistory)
    }

    override suspend fun delete(notifHistory: NotifHistoryEntry) {
        entries.remove(notifHistory)
    }

    override suspend fun deleteAll() {
        entries.clear()
    }

    override suspend fun update(notifHistory: NotifHistoryEntry) {
        entries.find { it.chatId == notifHistory.chatId }?.let { entries.remove(it) }
        entries.add(notifHistory)
    }

    override fun getEntry(chatId: String): NotifHistoryEntry? {
        return entries.find { it.chatId == chatId }
    }

}