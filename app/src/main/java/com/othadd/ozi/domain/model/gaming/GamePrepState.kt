package com.othadd.ozi.domain.model.gaming

import com.othadd.ozi.domain.model.User

data class GamePrepState(
    val requestCreationTime: Long,
    val host: User,
    val thisUserId: String,
    val invitees: List<User>,
    val declinedInviteesIds: List<String>,
    val acceptedInviteesIds: List<String>,
    val prepOutcome: GamePrepOutcome?
)
