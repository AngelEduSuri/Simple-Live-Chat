package com.aesuriagasalazar.simplelivechat.model

data class MessageFirebase(
    val uid: String = "",
    val message: String = "",
    val date: Long = 0
)