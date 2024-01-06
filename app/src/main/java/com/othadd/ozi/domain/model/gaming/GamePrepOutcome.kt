package com.othadd.ozi.domain.model.gaming

enum class GamePrepOutcome(val string: String) {
    STARTING_GAME("starting_game"),
    CANCELLING_NO_HOST_RESPONSE("cancelling_no_host_response"),
    HOST_CANCELLED("host_cancelled"),
    PROMPTING_HOST("prompting_host"),
    ZERO_ACCEPTANCE("zero_acceptance")
    ;

    companion object {
        fun getOutcome(string: String): GamePrepOutcome? {
            return values().find { it.string == string }
        }
    }
}
