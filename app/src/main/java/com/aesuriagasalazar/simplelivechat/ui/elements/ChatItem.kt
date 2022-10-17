package com.aesuriagasalazar.simplelivechat.ui.elements

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aesuriagasalazar.simplelivechat.model.Message

@Composable
fun ChatItem(
    modifier: Modifier = Modifier,
    message: Message,
    isUserMessage: Boolean,
    time: String
) {
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
                text = if (isUserMessage) time else message.author.name.plus(" • $time"),
                style = MaterialTheme.typography.body1.copy(
                    fontStyle = FontStyle.Italic,
                    fontSize = 13.sp
                )
            )
        }
    }
}
