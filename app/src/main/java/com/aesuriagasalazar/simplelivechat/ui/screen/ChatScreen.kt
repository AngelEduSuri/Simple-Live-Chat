package com.aesuriagasalazar.simplelivechat.ui.screen

import androidx.compose.animation.*
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Save
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aesuriagasalazar.simplelivechat.R
import com.aesuriagasalazar.simplelivechat.model.Message
import com.aesuriagasalazar.simplelivechat.ui.elements.ChatButtonSend
import com.aesuriagasalazar.simplelivechat.ui.elements.ChatItem
import com.aesuriagasalazar.simplelivechat.ui.elements.ChatSignMessage
import com.aesuriagasalazar.simplelivechat.ui.elements.ChatTextField
import com.aesuriagasalazar.simplelivechat.utils.convertToTimeAgo
import java.util.*

@Composable
fun ChatScreen(
    modifier: Modifier = Modifier,
    viewModel: ChatViewModel = viewModel()
) {

    val uiState by viewModel.chatUiState.collectAsState()

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column {
            ShowUserName(
                userName = uiState.currentName,
                isEditing = uiState.isEditingUserName,
                onStartEditing = viewModel::onEditingUserName,
                onEditUserName = viewModel::onUserNameChanged,
                onEditingDone = viewModel::onUpdateUserName,
                onCancelEditing = viewModel::onEditingUserName
            )
            MessageList(
                modifier = Modifier.weight(weight = 1f),
                messages = uiState.chats,
                userMessage = uiState.user.name
            )
            MessageButton(
                text = uiState.text,
                onTextChanged = viewModel::onMessageChanged,
                onIconClick = viewModel::onSendMessage
            )
        }
        if (uiState.isSendingMessage) CircularProgressIndicator()
    }
    ChatSignMessage(updatingUserName = uiState.isUpdatingUserName)

    LaunchedEffect(key1 = !uiState.isEditingUserName) {
        viewModel.checkUserState()
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ShowUserName(
    modifier: Modifier = Modifier,
    userName: String,
    isEditing: Boolean,
    onStartEditing: () -> Unit,
    onEditUserName: (String) -> Unit,
    onEditingDone: () -> Unit,
    onCancelEditing: () -> Unit
) {

    val keyboardController = LocalSoftwareKeyboardController.current

    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = modifier
                .height(height = 70.dp)
                .animateContentSize(
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = LinearEasing
                    )
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (isEditing) {
                IconButton(onClick = {
                    onCancelEditing()
                    keyboardController?.hide()
                }) {
                    Icon(imageVector = Icons.Rounded.Close, contentDescription = null)
                }
                OutlinedTextField(value = userName, onValueChange = onEditUserName)
                IconButton(onClick = {
                    onEditingDone()
                    keyboardController?.hide()
                }) {
                    Icon(imageVector = Icons.Rounded.Save, contentDescription = null)
                }
            } else {
                Text(text = userName, style = MaterialTheme.typography.h5.copy(fontSize = 20.sp))
                IconButton(onClick = onStartEditing) {
                    Icon(imageVector = Icons.Rounded.Edit, contentDescription = null)
                }
            }
        }
    }
}

@Composable
fun MessageList(
    modifier: Modifier = Modifier,
    messages: List<Message>,
    userMessage: String
) {

    val state = rememberLazyListState()

    LaunchedEffect(key1 = messages.size) {
        if (messages.isNotEmpty()) state.scrollToItem(index = messages.lastIndex)
    }

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(all = 8.dp),
        verticalArrangement = if (messages.isNotEmpty()) Arrangement.spacedBy(space = 8.dp) else Arrangement.Center,
        state = state,
    ) {
        if (messages.isNotEmpty()) {
            items(messages) {
                ChatItem(
                    message = it,
                    isUserMessage = userMessage == it.author.name,
                    time = convertToTimeAgo(it.timestamp)
                )
            }
        } else {
            item {
                BodyMessageEmpty()
            }
        }
    }
}

@Composable
fun BodyMessageEmpty(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier.width(width = 200.dp),
            backgroundColor = MaterialTheme.colors.secondary.copy(alpha = 0.4f)
                .compositeOver(
                    background = MaterialTheme.colors.background
                ),
            elevation = 8.dp,
            shape = MaterialTheme.shapes.small.copy(all = CornerSize(size = 8.dp)),
        ) {
            Column(
                modifier = Modifier.padding(all = 8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = stringResource(R.string.no_message))
                Spacer(modifier = Modifier.height(height = 16.dp))
                Text(text = stringResource(R.string.send_a_message))
                Spacer(modifier = Modifier.height(height = 16.dp))
                Image(
                    modifier = Modifier.size(size = 100.dp),
                    painter = painterResource(id = R.drawable.ic_baseline_space_dashboard_24),
                    contentDescription = null,
                    alpha = 0.5f
                )
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MessageButton(
    modifier: Modifier = Modifier,
    text: String,
    onTextChanged: (String) -> Unit,
    onIconClick: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ChatTextField(
            modifier = Modifier.weight(weight = 1f),
            text = text,
            onTextChanged = onTextChanged
        )
        Spacer(modifier = Modifier.width(width = 8.dp))
        ChatButtonSend(
            onIconClick = {
                onIconClick()
                keyboardController?.hide()
            },
            messageIsEmpty = text.isNotEmpty()
        )
    }
}
