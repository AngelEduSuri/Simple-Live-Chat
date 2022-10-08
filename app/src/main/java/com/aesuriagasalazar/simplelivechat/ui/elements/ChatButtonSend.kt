package com.aesuriagasalazar.simplelivechat.ui.elements

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ChatButtonSend(
    modifier: Modifier = Modifier,
    onIconClick: () -> Unit,
    messageIsEmpty: Boolean
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(percent = 100),
        backgroundColor = MaterialTheme.colors.primary
    ) {
        IconButton(
            onClick = onIconClick,
            enabled = messageIsEmpty
        ) {
            Icon(
                imageVector = Icons.Default.Send,
                contentDescription = null,
            )
        }
    }
}