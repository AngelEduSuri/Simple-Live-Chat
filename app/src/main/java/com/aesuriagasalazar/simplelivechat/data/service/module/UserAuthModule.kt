package com.aesuriagasalazar.simplelivechat.data.service.module

import com.aesuriagasalazar.simplelivechat.data.datasources.UserRemoteDataSource
import com.aesuriagasalazar.simplelivechat.data.service.impl.UserFirebaseAuthDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class UserAuthModule {

    @Binds
    abstract fun bindUserAuthService(impl: UserFirebaseAuthDataSourceImpl): UserRemoteDataSource
}