@file:Suppress("NonAsciiCharacters")

package com.neesan.presentation.player.component

import androidx.annotation.OptIn
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.compose.state.rememberPlayPauseButtonState
import com.neesan.presentation.isPreview

@OptIn(UnstableApi::class)
@Composable
internal fun PlayPauseButtonComponent(player: Player?, modifier: Modifier = Modifier) {
    if (isPreview()) {
        // Previewモードでは何もしない
        PlayPauseIconButton(
            modifier = modifier,
            icon = Icons.Default.PlayArrow,
            contentDescription = "preview",
            onClick = {}
        )
        return
    }

    if (player == null) {
        // プレイヤーがnullの場合は何もしない
        return
    }

    val state = rememberPlayPauseButtonState(player)
    val icon = if (state.showPlay) Icons.Default.PlayArrow else Icons.Filled.Pause
    val contentDescription =
        if (state.showPlay) "再生ボタン"
        else "一時停止ボタン"
    PlayPauseIconButton(modifier, icon, contentDescription, onClick = { state.onClick() })
}

@Composable
private fun PlayPauseIconButton(
    modifier: Modifier,
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
) {
    IconButton(
        modifier = modifier,
        onClick = onClick,
    ) {
        Icon(
            icon,
            contentDescription = contentDescription,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPlayPauseButton_再生() {
    PlayPauseIconButton(
        modifier = Modifier.size(24.dp),
        icon = Icons.Default.PlayArrow,
        contentDescription = "preview",
        onClick = {}
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewPlayPauseButton_一時停止() {
    PlayPauseIconButton(
        modifier = Modifier.size(24.dp),
        icon = Icons.Default.Pause,
        contentDescription = "preview",
        onClick = {}
    )
}