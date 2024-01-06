package com.othadd.ozi.domain.model

enum class SignalType(val id: String, val nameX: String) {
    NEW_MESSAGE("new_message_signal_id", "new_message_signal_type"),
    GAME_REQUEST("game_request_signal_id", "game_request_signal_type"),
    GAME_BROKER_UPDATE("game_broker_update_id", "game_broker_update_signal_type"),
    GAME_MODERATOR_UPDATE("game_moderator_update_id", "game_moderator_update_type")
    ;

    companion object {
        fun getSignalType(id: String): SignalType? {
            return values().find { it.id == id }
        }
    }
}