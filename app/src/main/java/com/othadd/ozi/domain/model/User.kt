package com.othadd.ozi.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey val userId: String,
    var username: String,
    var aviFg: Int,
    val aviBg: Int,
    var online: Boolean,
    var verified: Boolean,
    val token: String?,
    val gameState: String
)