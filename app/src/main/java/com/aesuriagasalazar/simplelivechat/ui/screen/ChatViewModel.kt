package com.aesuriagasalazar.simplelivechat.ui.screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aesuriagasalazar.simplelivechat.data.repositories.ChatRepository
import com.aesuriagasalazar.simplelivechat.model.Message
import com.aesuriagasalazar.simplelivechat.model.Response
import com.aesuriagasalazar.simplelivechat.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(private val chatRepository: ChatRepository) : ViewModel() {

    private val _chatUiState = MutableStateFlow(ChatUiState())
    val chatUiState: StateFlow<ChatUiState> = _chatUiState.asStateFlow()

    fun checkUserState() = viewModelScope.launch {
        chatRepository.checkUserInFirebase().collectResponse(
            onLoading = null,
            onSuccess = {
                _chatUiState.update { uiState -> uiState.copy(user = it, currentName = it.name) }
            },
            onFailure = {
                Log.i(this@ChatViewModel.javaClass.simpleName, "Failure: $it")
            }
        )
    }

    init {
        checkAllChat()
    }

    private fun checkAllChat() = viewModelScope.launch {
        chatRepository.getChat().collectResponse(
            onLoading = null,
            onSuccess = { listMessage ->
                val sorted = listMessage.asSequence().sortedBy { it?.timestamp }.toList()
                _chatUiState.update {
                    it.copy(chats = sorted)
                }
            },
            onFailure = {
                Log.i(this@ChatViewModel.javaClass.simpleName, "Failure: $it")
            }
        )
    }

    fun onMessageChanged(textChanged: String) {
        _chatUiState.update {
            it.copy(text = textChanged)
        }
    }

    fun onUserNameChanged(nameChanged: String) {
        _chatUiState.update {
            it.copy(currentName = nameChanged)
        }
    }

    fun onUpdateUserName() = viewModelScope.launch {
        chatRepository.updateUserName(chatUiState.value.currentName).collectResponse(
            onLoading = { onUpdatingUserName() },
            onSuccess = {
                _chatUiState.update { uiState -> uiState.copy(user = it) }
                updateMyUserNameInAllChat()
                onEditingUserName()
            },
            onFailure = {
                Log.i(this@ChatViewModel.javaClass.simpleName, "Failure: $it")
            }
        )
        onUpdatingUserName()
    }

    fun onSendMessage() = viewModelScope.launch {
        val message =
            Message(chatUiState.value.user, chatUiState.value.text, System.currentTimeMillis())
        chatRepository.sendMessageInFirebase(message).collectResponse(
            onLoading = { onSendingMessage() },
            onSuccess = {
                if (it) _chatUiState.update { uiState -> uiState.copy(text = "") }
            },
            onFailure = {
                Log.i(this@ChatViewModel.javaClass.simpleName, "Failure: $it")
            }
        )
        onSendingMessage()
    }

    private suspend fun <T> Flow<Response<T>>.collectResponse(
        onLoading: (() -> Unit)?,
        onSuccess: (T) -> Unit,
        onFailure: (Exception?) -> Unit
    ) {
        collect {
            when (it) {
                is Response.Loading -> onLoading?.invoke()
                is Response.Success -> onSuccess(it.value)
                is Response.Failure -> onFailure(it.exception)
            }
        }
    }

    fun onEditingUserName() {
        _chatUiState.update {
            it.copy(isEditingUserName = !it.isEditingUserName)
        }
    }

    private fun onUpdatingUserName() {
        _chatUiState.update {
            it.copy(isUpdatingUserName = !it.isUpdatingUserName)
        }
    }

    private fun onSendingMessage() {
        _chatUiState.update {
            it.copy(isSendingMessage = !it.isSendingMessage)
        }
    }

    private fun updateMyUserNameInAllChat() = viewModelScope.launch {
        val chats =
            chatUiState.value.chats.map {
                if (it.author.uid == chatUiState.value.user.uid) {
                    it.copy(author = chatUiState.value.user)
                } else {
                    it
                }
            }
        _chatUiState.update { it.copy(chats = chats) }
    }
}

data class ChatUiState(
    val chats: List<Message> = emptyList(),
    val text: String = "",
    val currentName: String = "",
    val user: User = User(),
    val isEditingUserName: Boolean = false,
    val isUpdatingUserName: Boolean = false,
    val isSendingMessage: Boolean = false,
)