package com.neesan.presentation.player.component

import androidx.annotation.OptIn
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.compose.state.rememberPlayPauseButtonState

@OptIn(UnstableApi::class)
@Composable
internal fun PlayPauseButtonComponent(player: Player?, modifier: Modifier = Modifier) {
    if (player == null) {
        // プレイヤーがnullの場合は何もしない
        return
    }

    val state = rememberPlayPauseButtonState(player)
    val icon = if (state.showPlay) Icons.Default.PlayArrow else Icons.Filled.Pause
    val contentDescription =
        if (state.showPlay) "再生ボタン"
        else "一時停止ボタン"
    Icon(
        icon,
        contentDescription = contentDescription,
        modifier = modifier.clickable(enabled = state.isEnabled, onClick = { state.onClick() }),
    )
}