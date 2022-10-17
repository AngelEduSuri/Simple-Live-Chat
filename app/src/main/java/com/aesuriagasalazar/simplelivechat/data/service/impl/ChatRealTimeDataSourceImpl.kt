package com.aesuriagasalazar.simplelivechat.data.service.impl

import com.aesuriagasalazar.simplelivechat.data.datasources.ChatRemoteDataSource
import com.aesuriagasalazar.simplelivechat.model.Message
import com.aesuriagasalazar.simplelivechat.model.MessageFirebase
import com.aesuriagasalazar.simplelivechat.model.Response
import com.aesuriagasalazar.simplelivechat.model.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

private const val PATH = "chat"

class ChatRealTimeDataSourceImpl @Inject constructor(private val database: DatabaseReference) :
    ChatRemoteDataSource {

    override suspend fun getAllChat() = callbackFlow {

        val valueChanged = object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                val listOfMessages = mutableListOf<MessageFirebase>()
                val listOfUser = mutableListOf<User>()

                // Loop to message node that contains user uid that its key
                snapshot.children.forEach { usersId ->
                    usersId.key?.let { key ->
                        // Get user that is the same that userId.key
                        val userDeferred = async(Dispatchers.IO) {
                            database.child(PATH).child("users")
                                .child(key).get().await()?.let {
                                    listOfUser.add(
                                        User(
                                            key,
                                            it.getValue(String::class.java) ?: ""
                                        )
                                    )
                                }
                            return@async listOfUser
                        }

                        // Get all messages
                        usersId.children.forEach {
                            it.getValue(MessageFirebase::class.java)?.let { ms ->
                                listOfMessages.add(ms.copy(uid = key))
                            }
                        }

                        launch {
                            // Wait for list of users
                            val users = userDeferred.await()
                            // Map firebase message to message domain
                            val messageList = listOfMessages.map { lm ->
                                users.find { it.uid == lm.uid }?.let {
                                    Message(
                                        author = it,
                                        lm.message,
                                        lm.date
                                    )
                                } ?: Message(User(), "", 0)
                            }
                            // Try send messages
                            this@callbackFlow.trySend(Response.Success(messageList))
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                this@callbackFlow.trySend(Response.Failure(error.toException()))
            }
        }
        database.child(PATH).child("messages").addValueEventListener(valueChanged)

        awaitClose {
            database.child(PATH).child("messages").removeEventListener(valueChanged)
        }
    }

    override suspend fun createMessage(message: Message) = flow {
        emit(Response.Loading)
        val value = mapOf(
            "message" to message.body,
            "date" to message.timestamp
        )
        try {
            database.child(PATH)
                .child("messages")
                .child(message.author.uid)
                .push()
                .setValue(value)
                .await()
            emit(Response.Success(true))
        } catch (e: Exception) {
            emit(Response.Failure(e))
        }
    }
}