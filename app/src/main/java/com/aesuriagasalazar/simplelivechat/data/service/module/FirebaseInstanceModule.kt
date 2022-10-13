package com.aesuriagasalazar.simplelivechat.data.service.module

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object FirebaseInstanceModule {

    @Provides
    fun firebaseAuthInstance() = Firebase.auth

    @Provides
    fun firebaseDatabaseInstance() = FirebaseDatabase.getInstance().reference
}