package com.othadd.ozi.domain.model

enum class OziNotificationChannel(val id: String) {
    CHAT("chat_channel"),
    GAME("game_channel"),
    SILENT("silent_channel")
}

enum class NotificationType {
    CHAT,
    GAME
}