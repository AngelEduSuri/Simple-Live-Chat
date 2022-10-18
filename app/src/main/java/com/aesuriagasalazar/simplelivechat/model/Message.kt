package com.aesuriagasalazar.simplelivechat.model

data class Message(
    val author: User,
    val body: String,
    val timestamp: Long,
)


