package com.iyke.ozix.data.repos

import com.iyke.ozix.data.dataSources.database.daos.NotifHistoryDao
import com.iyke.ozix.domain.model.NotifHistoryEntry
import com.iyke.ozix.domain.repos.NotifHistoryRepo
import javax.inject.Inject

class NotifHistoryRepoImpl @Inject constructor(private val notifHistoryDao: NotifHistoryDao):
    NotifHistoryRepo {
    override fun getEntry(chatId: String): NotifHistoryEntry? {
        return notifHistoryDao.getEntry(chatId)
    }

    override suspend fun updateEntry(notifHistoryEntry: NotifHistoryEntry) {
        notifHistoryDao.update(notifHistoryEntry)
    }

    override suspend fun wipeEntries() {
        notifHistoryDao.deleteAll()
    }

    override suspend fun addEntry(notifHistoryEntry: NotifHistoryEntry) {
        notifHistoryDao.insert(notifHistoryEntry)
    }
}