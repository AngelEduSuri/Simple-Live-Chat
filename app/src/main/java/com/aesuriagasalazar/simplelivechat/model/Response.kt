package com.aesuriagasalazar.simplelivechat.model

sealed class Response<out T> {
    object Loading: Response<Nothing>()
    data class Success<out T>(val value: T): Response<T>()
    data class Failure(val exception: Exception?): Response<Nothing>()
}
