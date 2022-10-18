package com.aesuriagasalazar.simplelivechat.utils

private const val SECOND_MILLIS = 1000
private const val MINUTE_MILLIS = 60 * SECOND_MILLIS
private const val HOUR_MILLIS = 60 * MINUTE_MILLIS
private const val DAY_MILLIS = 24 * HOUR_MILLIS

fun convertToTimeAgo(timestamp: Long): String {
    val now = System.currentTimeMillis()
    if (timestamp > now || timestamp <= 0) {
        return "in the future"
    }
    val diff = now - timestamp
    return if (diff < MINUTE_MILLIS) {
        "Moments ago"
    } else if (diff < 2 * MINUTE_MILLIS) {
        "A minute ago"
    } else if (diff < 50 * MINUTE_MILLIS) {
        "${diff / MINUTE_MILLIS} minutes ago"
    } else if (diff < 90 * MINUTE_MILLIS) {
        "An hour ago"
    } else if (diff < 24 * HOUR_MILLIS) {
        "${diff / HOUR_MILLIS} hours ago"
    } else if (diff < 48 * HOUR_MILLIS) {
        "Yesterday"
    } else {
        "${diff / DAY_MILLIS} days ago"
    }
}