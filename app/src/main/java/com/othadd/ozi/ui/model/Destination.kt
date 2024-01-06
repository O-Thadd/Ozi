package com.othadd.ozi.ui.model

sealed class Destination(val route: String) {

    object HOME: Destination("home")

    object CHAT: Destination("chat")

    object PROFILE: Destination("profile")

    object EXPLORE: Destination("explore")

    object GROUP_GAME_SETUP: Destination("group_game_setup")

    object DEVELOPER: Destination("developer")

    sealed class ONBOARDING(route: String): Destination(route){

        companion object{
            const val route = "onBoarding"
        }

        object LANDING: ONBOARDING("landing")

        object REGISTER: ONBOARDING("register")

        object LOGIN: ONBOARDING("login")

    }

}