package com.iyke.ozix.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.iyke.ozix.domain.model.gaming.UserGameState

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

data class FromApiUser(
    val username: String,
    val userId: String,
    val aviFg: Int,
    val aviBg: Int,
)

data class ToApiUser(
    val username: String,
    val userId: String,
    val aviFg: Int,
    val aviBg: Int,
    val fcmToken: String
)

fun FromApiUser.toUser() = User(
    userId = userId,
    username = username,
    aviFg = aviFg,
    aviBg = aviBg,
    online = false,
    verified = false,
    token = userId,
    gameState = UserGameState.AVAILABLE.string
)

fun User.toApiUser() = ToApiUser(
    username = username,
    userId = userId,
    aviFg = aviFg,
    aviBg = aviBg,
    fcmToken = ""
)