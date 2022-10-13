package com.aesuriagasalazar.simplelivechat.data.datasources

import com.aesuriagasalazar.simplelivechat.model.Response
import com.aesuriagasalazar.simplelivechat.model.User
import kotlinx.coroutines.flow.Flow

interface UserRemoteDataSource {
    suspend fun checkUser(): Flow<Response<User>>
    suspend fun updateUserName(name: String): Flow<Response<User>>
}