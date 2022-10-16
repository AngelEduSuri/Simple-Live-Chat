package com.aesuriagasalazar.simplelivechat.ui.elements

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.aesuriagasalazar.simplelivechat.R

@Composable
fun ChatSignMessage(
    modifier: Modifier = Modifier,
    updatingUserName: Boolean,
    enterAnimation: EnterTransition = slideInVertically(),
    exitAnimation: ExitTransition = slideOutVertically()
) {
    AnimatedVisibility(
        modifier = modifier,
        visible = updatingUserName,
        enter = enterAnimation,
        exit = exitAnimation
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
                Text(
                    text = stringResource(R.string.updating_name),
                    style = MaterialTheme.typography.body1
                )
            }
        }
    }
}