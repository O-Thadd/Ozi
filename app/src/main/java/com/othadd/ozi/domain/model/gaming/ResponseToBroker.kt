package com.othadd.ozi.domain.model.gaming

enum class ResponseToBroker(var string: String) {
    ACCEPT("accept_game"),
    DECLINE("decline_game"),
    PROCEED("proceed"),
    CANCEL("cancel_game");

    companion object {
        fun getResponse(responseString: String?): ResponseToBroker? {
            return values().find { it.string == responseString }
        }
    }
}