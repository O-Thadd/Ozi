package com.othadd.ozi.domain.model.gaming

enum class GamingCommunicationTarget(val string: String) {
    SERVICE("gaming_service"),
    BROKER("game_broker"),
    MODERATOR("game_moderator");

    companion object {
        fun getTarget(string: String?): GamingCommunicationTarget? {
            return values().find { it.string == string }
        }
    }
}