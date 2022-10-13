package com.aesuriagasalazar.simplelivechat.data.service.impl

import com.aesuriagasalazar.simplelivechat.data.datasources.UserRemoteDataSource
import com.aesuriagasalazar.simplelivechat.model.Response
import com.aesuriagasalazar.simplelivechat.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

private const val DEFAULT_USER_NAME = "Default User Name"
private const val PATH = "chat"

class UserFirebaseAuthDataSourceImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val database: DatabaseReference
) :
    UserRemoteDataSource {

    override suspend fun checkUser() = flow {
        emit(Response.Loading)
        auth.currentUser.let { firebaseUser ->
            if (firebaseUser == null) {
                // Create a new User
                try {
                    auth.signInAnonymously().await().user?.let {
                        saveUserName(it)
                        currentUser(it)
                    }
                } catch (e: Exception) {
                    emit(Response.Failure(e))
                }
            } else {
                // Otherwise get current user
                currentUser(firebaseUser)
            }
        }
    }

    private suspend fun FlowCollector<Response<User>>.currentUser(
        firebaseUser: FirebaseUser
    ) {
        val user = User(
            firebaseUser.uid,
            firebaseUser.displayName?.ifEmpty { DEFAULT_USER_NAME } ?: DEFAULT_USER_NAME)
        emit(Response.Success(user))
    }

    override suspend fun updateUserName(name: String) = flow {
        emit(Response.Loading)
        try {
            auth.currentUser?.let {
                // Request for update display name
                val userUpdate = UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .build()
                it.updateProfile(userUpdate).await()
                saveUserName(it)
                emit(Response.Success(User(it.uid, it.displayName!!)))
            }
            // Return true when is completed
        } catch (e: Exception) {
            // If it's fails, then return the exception
            emit(Response.Failure(e))
        }
    }

    // Save user in realtime database in users node
    private suspend fun saveUserName(user: FirebaseUser) = database.child(PATH)
        .child("users")
        .child(user.uid)
        .setValue(user.displayName ?: DEFAULT_USER_NAME).await()
}

