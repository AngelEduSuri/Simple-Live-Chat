package com.aesuriagasalazar.simplelivechat.model

import org.ocpsoft.prettytime.PrettyTime
import java.util.*

data class Message(
    val author: User,
    val body: String,
    val timestamp: Long,
) {
    fun convertToDate(): String = PrettyTime(Locale.getDefault()).format(Date(timestamp))
}


