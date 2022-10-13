package com.aesuriagasalazar.simplelivechat.data.service.module

import com.aesuriagasalazar.simplelivechat.data.datasources.ChatRemoteDataSource
import com.aesuriagasalazar.simplelivechat.data.service.impl.ChatRealTimeDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ChatModule {

    @Binds
    abstract fun bindChatDatabaseService(impl: ChatRealTimeDataSourceImpl): ChatRemoteDataSource

}