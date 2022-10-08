package com.aesuriagasalazar.simplelivechat.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Save
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aesuriagasalazar.simplelivechat.R
import com.aesuriagasalazar.simplelivechat.model.Message
import com.aesuriagasalazar.simplelivechat.ui.elements.ChatButtonSend
import com.aesuriagasalazar.simplelivechat.ui.elements.ChatItem
import com.aesuriagasalazar.simplelivechat.ui.elements.ChatTextField

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
                userName = uiState.user.name,
                isEditing = uiState.isUserNameEditing,
                onStartEditing = viewModel::onStartEditUserName,
                onEditUserName = viewModel::onUserNameChanged,
                onEditingDone = viewModel::onUpdateUserName
            )
            MessageList(
                modifier = Modifier.weight(weight = 1f),
                messages = uiState.chats
            )
            MessageBottom(
                text = uiState.text,
                onTextChanged = viewModel::onMessageChanged,
                onIconClick = viewModel::onSendMessage
            )
        }
        if (uiState.isSendingMessage) LinearProgressIndicator()
    }
    AnimatedVisibility(
        visible = uiState.isSavingNameUpdated,
        enter = slideInVertically(),
        exit = slideOutVertically()
    ) {
        Card(backgroundColor = MaterialTheme.colors.secondary) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height = 70.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.width(width = 8.dp))
                Text(text = stringResource(R.string.updating_name), style = MaterialTheme.typography.body1)
            }
        }
    }

    LaunchedEffect(key1 = !uiState.isUserNameEditing) {
        viewModel.checkUserState()
    }
}

@Composable
fun ShowUserName(
    modifier: Modifier = Modifier,
    userName: String,
    isEditing: Boolean,
    onStartEditing: () -> Unit,
    onEditUserName: (String) -> Unit,
    onEditingDone: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(all = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        if (isEditing) {
            OutlinedTextField(value = userName, onValueChange = onEditUserName)
            IconButton(onClick = onEditingDone) {
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

@Composable
fun MessageList(modifier: Modifier = Modifier, messages: List<Message>) {

    val state = rememberLazyListState()

    LaunchedEffect(key1 = messages.size) {
        if (messages.isNotEmpty()) state.scrollToItem(index = messages.lastIndex)
    }

    if (messages.isNotEmpty()) {
        LazyColumn(
            modifier = modifier,
            contentPadding = PaddingValues(all = 8.dp),
            verticalArrangement = Arrangement.spacedBy(space = 8.dp),
            state = state,
        ) {
            items(items = messages) {
                ChatItem(message = it, isUserMessage = true)
            }
        }
    } else {
        BodyMessageEmpty(modifier = modifier)
    }
}

@Composable
fun BodyMessageEmpty(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
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

@Composable
fun MessageBottom(
    modifier: Modifier = Modifier,
    text: String,
    onTextChanged: (String) -> Unit,
    onIconClick: () -> Unit
) {
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
            onIconClick = onIconClick,
            messageIsEmpty = text.isNotEmpty()
        )
    }
}
