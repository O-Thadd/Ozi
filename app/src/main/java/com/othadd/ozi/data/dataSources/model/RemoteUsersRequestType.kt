package com.othadd.ozi.data.dataSources.model

enum class RemoteUsersRequestType(val string: String) {
    SPECIFIC("specific"),
    SUGGESTED("suggested"),
    SEARCH("search")
    ;

    companion object {
        fun getType(string: String): RemoteUsersRequestType? {
            return values().find { it.string == string }
        }
    }
}