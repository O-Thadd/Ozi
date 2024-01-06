package com.othadd.ozi.data.dataSources.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.othadd.ozi.domain.model.User
import com.othadd.ozi.domain.model.message.Message
import com.othadd.ozi.domain.model.NotifHistoryMessage
import java.lang.reflect.Type

class Converters {

    @TypeConverter
    fun messagesToString(messages: List<Message>): String{
        val gson = Gson()
        return gson.toJson(messages)
    }

    @TypeConverter
    fun stringToMessages(jsonString: String): List<Message> {
        val gson = Gson()
        val collectionType: Type = object : TypeToken<List<Message?>?>() {}.type
        return gson.fromJson(jsonString, collectionType)
    }

    @TypeConverter
    fun stringToUser(jsonString: String): User {
        return Gson().fromJson(jsonString, User::class.java)
    }

    @TypeConverter
    fun userToString(user: User): String{
        return Gson().toJson(user)
    }

    @TypeConverter
    fun stringListToString(strings: List<String>): String{
        val gson = Gson()
        return gson.toJson(strings)
    }

    @TypeConverter
    fun stringToStringList(jsonString: String): List<String> {
        val gson = Gson()
        val collectionType: Type = object : TypeToken<List<String?>?>() {}.type
        return gson.fromJson(jsonString, collectionType)
    }

    @TypeConverter
    fun notifHistoryMessagesToString(strings: List<NotifHistoryMessage>): String{
        val gson = Gson()
        return gson.toJson(strings)
    }

    @TypeConverter
    fun stringToNotifHistoryMessages(jsonString: String): List<NotifHistoryMessage> {
        val gson = Gson()
        val collectionType: Type = object : TypeToken<List<NotifHistoryMessage?>?>() {}.type
        return gson.fromJson(jsonString, collectionType)
    }
}