package com.aesuriagasalazar.simplelivechat.ui.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aesuriagasalazar.simplelivechat.model.Message
import com.aesuriagasalazar.simplelivechat.model.User
import com.aesuriagasalazar.simplelivechat.ui.theme.SimpleLiveChatTheme

@Composable
fun ChatItem(modifier: Modifier = Modifier, message: Message, isUserMessage: Boolean) {

    val current = LocalConfiguration.current

    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = if (isUserMessage) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        Column(
            modifier = Modifier
                .wrapContentHeight()
                .widthIn(max = (current.screenWidthDp * 0.8).dp),
            horizontalAlignment = if (isUserMessage) Alignment.End else Alignment.Start
        ) {
            Card(
                elevation = 12.dp,
                shape = MaterialTheme.shapes.small.copy(all = CornerSize(size = 8.dp))
            ) {
                Box(modifier = Modifier.padding(all = 8.dp)) {
                    Text(
                        text = message.body, style = MaterialTheme.typography.body1
                    )
                }
            }
            Text(
                text = "User: ".plus(message.author.name),
                style = MaterialTheme.typography.body1.copy(
                    fontStyle = FontStyle.Italic,
                    fontSize = 13.sp
                )
            )
        }
    }
}

@Preview(name = "Message Preview", widthDp = 350, showBackground = true)
@Composable
fun ChatItemPreview() {
    SimpleLiveChatTheme {
        Column(modifier = Modifier.padding(all = 8.dp)) {
            ChatItem(
                message = Message(
                    author = User(name = "Angel"),
                    body = "Hi, this is a preview message for to check the design",
                    timestamp = 0
                ), isUserMessage = false
            )
            Spacer(modifier = Modifier.height(height = 8.dp))
            ChatItem(
                message = Message(
                    author = User(name = "Eduardo"),
                    body = "Hi, this is a second preview message",
                    timestamp = 0
                ), isUserMessage = true
            )
        }
    }
}