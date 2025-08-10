package com.neesan.presentation.player.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.media3.exoplayer.ExoPlayer
import com.neesan.presentation.isPreview
import kotlinx.coroutines.delay

@Composable
fun SeekBarComponent(modifier: Modifier, player: ExoPlayer?) {
    val duration = if (isPreview()) 0L else player?.duration!!
    var position by remember { mutableLongStateOf(0L) }

    LaunchedEffect(Unit) {
        while (true) {
            position = player?.currentPosition!!
            delay(100L)
        }
    }

    SeekBarContent(
        position = position,
        onValueChange = { player?.seekTo(it)!! },
        duration = duration,
        modifier = modifier
    )
}

/**
 * プレビューを機能させるために、ComponentとContentを分離
 * こちらはプレビューランタイムで解決できない外部依存がなく、プレビュー可能
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SeekBarContent(
    modifier: Modifier,
    position: Long,
    duration: Long,
    onValueChange: (Long) -> Unit
) {
    val valueRangeEnd = if (duration > 0L) duration else 0L // durationが0以下なら0fにする
    val adjustedPosition = position.coerceIn(0L, valueRangeEnd) // position も duration の範囲内に収める

    Slider(
        value = adjustedPosition.toFloat(),
        onValueChange = { onValueChange(it.toLong()) },
        valueRange = 0f..valueRangeEnd.toFloat(), // valueRangeEnd が常に 0f 以上であることを保証
        modifier = modifier,
        thumb = {
            Icon(
                imageVector = Icons.Filled.Circle,
                contentDescription = null,
                modifier = Modifier.size(ButtonDefaults.IconSize),
                tint = Color.Gray
            )
        }
    )
}

@Preview
@Composable
fun PreviewSeekBarContent() {
    SeekBarContent(
        modifier = Modifier.fillMaxWidth(),
        position = 5000L,
        duration = 30000L,
        onValueChange = {}
    )
}