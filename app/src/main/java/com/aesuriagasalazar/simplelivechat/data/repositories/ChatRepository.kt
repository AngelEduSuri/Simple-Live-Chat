package com.aesuriagasalazar.simplelivechat.data.repositories

import com.aesuriagasalazar.simplelivechat.data.datasources.ChatRemoteDataSource
import com.aesuriagasalazar.simplelivechat.data.datasources.UserRemoteDataSource
import com.aesuriagasalazar.simplelivechat.model.Message
import javax.inject.Inject

class ChatRepository @Inject constructor(
    private val userRemoteDataSource: UserRemoteDataSource,
    private val chatRemoteDataSource: ChatRemoteDataSource
) {

    suspend fun getChat() = chatRemoteDataSource.getAllChat()

    suspend fun checkUserInFirebase() = userRemoteDataSource.checkUser()

    suspend fun updateUserName(name: String) = userRemoteDataSource.updateUserName(name)

    suspend fun sendMessageInFirebase(message: Message) = chatRemoteDataSource.createMessage(message)
}