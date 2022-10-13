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

                    // Get user that is the same that userId.key
                    val userDeferred = async(Dispatchers.IO) {
                        val result = database.child(PATH).child("users")
                            .child(usersId.key!!).get().await()
                        listOfUser.add(
                            User(
                                usersId.key ?: "",
                                result.getValue(String::class.java) ?: ""
                            )
                        )
                        return@async listOfUser
                    }
                    // Get all messages
                    val messagesDeferred = async(Dispatchers.IO) {
                        usersId.children.forEach {
                            it.getValue(MessageFirebase::class.java)?.let { ms ->
                                listOfMessages.add(ms.copy(uid = usersId.key!!))
                            }
                        }
                        return@async listOfMessages
                    }

                    launch {
                        val message = messagesDeferred.await()
                        val user = userDeferred.await()

                        val messageList = message.map { msg ->
                            Message(user.find { it.uid == msg.uid } ?: User(),
                                msg.message,
                                msg.date)
                        }

                        this@callbackFlow.trySend(Response.Success(messageList))
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