package com.aesuriagasalazar.simplelivechat.data.datasources

import com.aesuriagasalazar.simplelivechat.model.Message
import com.aesuriagasalazar.simplelivechat.model.Response
import com.google.firebase.database.DataSnapshot
import kotlinx.coroutines.flow.Flow

interface ChatRemoteDataSource {
    suspend fun getAllChat(): Flow<Response<List<Message>>>
    suspend fun createMessage(message: Message): Flow<Response<Boolean>>
}