package com.iyke.ozix.data.dataSources.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.iyke.ozix.data.dataSources.database.daos.ChatDao
import com.iyke.ozix.data.dataSources.database.daos.MessageDao
import com.iyke.ozix.data.dataSources.database.daos.NotifHistoryDao
import com.iyke.ozix.data.dataSources.database.daos.UserDao
import com.iyke.ozix.domain.model.NotifHistoryEntry
import com.iyke.ozix.domain.model.User
import com.iyke.ozix.domain.model.chat.Chat
import com.iyke.ozix.domain.model.message.Message

@Database(entities = [Chat::class, Message::class, User::class, NotifHistoryEntry::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class OziDatabase : RoomDatabase() {

    abstract fun chatDao(): ChatDao
    abstract fun userDao(): UserDao
    abstract fun messageDao(): MessageDao
    abstract fun notifHistoryDao(): NotifHistoryDao

    companion object {
        @Volatile
        private var INSTANCE: OziDatabase? = null
        fun getDatabase(context: Context): OziDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    OziDatabase::class.java,
                    "ozi_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}