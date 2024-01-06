package com.othadd.ozi.common

enum class Params(var string: String) {
    USER_ID("userId"),
    USERNAME("username"),
    PASSWORD("password"),
    AVI_FG("aviFg"),
    AVI_BG("aviBg"),
    FCM_TOKEN("fcmToken"),
    SEARCH_TERM("searchTerm"),
    REQUEST_TYPE("requestType"),
    CHAT_ID("chatId"),
    LAST_MESSAGE_ID("lastMessageId"),
    LAST_MESSAGE_TIMESTAMP("lastMessageTimestamp"),
    MESSAGE("message"),
    MESSAGE_META("messageMeta"),
    GAMING_DATA("gaming_data"),
    SERVICE_TARGET("service_target"),
    GAMING_COMMUNICATION_TARGET("gaming_communication_target")
}