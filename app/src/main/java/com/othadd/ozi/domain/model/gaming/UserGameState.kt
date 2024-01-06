package com.othadd.ozi.domain.model.gaming

enum class UserGameState(var string: String) {
    PLAYING("playing_game_state"),
    PENDING_REQUEST("pending_request_game_state"),
    AVAILABLE("available_game_state");
}