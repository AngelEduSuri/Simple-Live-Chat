package com.aesuriagasalazar.simplelivechat.ui.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.aesuriagasalazar.simplelivechat.R

@Composable
fun ChatTopBar(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(height = 56.dp),
        elevation = if (isSystemInDarkTheme()) 0.dp else 8.dp
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                modifier = Modifier.padding(all = 8.dp),
                painter = painterResource(id = R.drawable.chat_app_icon),
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(width = 16.dp))
            Text(
                text = stringResource(R.string.live_chat),
                style = MaterialTheme.typography.h6
            )
        }
    }
}