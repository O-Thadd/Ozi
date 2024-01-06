package com.othadd.ozi.common

enum class JsonResponseField(val string: String) {
    STATUS("status"),
    TOKEN("token"),
    USER_ID("userId"),
    GAME_BROKER_ID("game_broker_id"),
    GAME_HOST_ID("game_host_id"),
    GAME_INVITEES("game_invitees"),
    DECLINING_GAME_INVITEES("declining_game_invitees"),
    ACCEPTING_GAME_INVITEES("accepting_game_invitees"),
    PENDING_GAME_INVITEES("pending_game_invitees"),
    GAME_BROKER_OUTCOME("game_broker_outcome"),
    GAME_MODERATOR_ID("game_moderator_id"),
    GAME_REQUEST_CREATION_TIME("game_request_creation_time"),
    GAME_BROKER_PROMPT("game_broker_prompt"),
    GAME_CHAT_ID("game_chat_id"),
}