package com.neesan.presentation.player.section

import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackParameters
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.hls.HlsMediaSource
import com.neesan.core.valueClass.PitchKey
import com.neesan.presentation.isPreview
import com.neesan.presentation.player.component.PitchControllerComponent
import com.neesan.presentation.player.component.PlayPauseButtonComponent
import com.neesan.presentation.player.component.SeekBarComponent
import kotlin.math.pow

@OptIn(UnstableApi::class)
@Composable
fun PlayerSection(
    streamingUrl: String?,
    currentKey: PitchKey,
    onPitchUp: () -> Unit,
    onPitchDown: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val isPreview = isPreview()

    val player = remember {
        if (isPreview) {
            // PreviewモードではExoPlayerを使用しない
            return@remember null
        } else {
            ExoPlayer.Builder(context).build()
        }
    }

    // ストリーミングURLが検出されたらExoPlayerでHLS MediaSourceをセットアップ
    LaunchedEffect(streamingUrl) {
        streamingUrl?.let { url ->
            // HLS用のDataSourceFactoryを作成
            val dataSourceFactory = DefaultHttpDataSource.Factory()

            // HLS MediaSourceを作成
            val hlsMediaSource = HlsMediaSource.Factory(dataSourceFactory)
                .setAllowChunklessPreparation(false)
                .createMediaSource(MediaItem.fromUri(url))

            // ExoPlayerにHLS MediaSourceを設定
            player?.setMediaSource(hlsMediaSource)
            player?.prepare()
            // メディア準備完了時点の currentKey を必ず反映する
            // （LaunchedEffect(currentKey) との実行順が前後しても確実に適用するための保険）
            updatePitch(player, currentKey)
        }
    }

    LaunchedEffect(currentKey) {
        updatePitch(player, currentKey)
    }

    // プレイヤーのクリーンアップ
    DisposableEffect(player) {
        onDispose {
            player?.release()
        }
    }

    Column(modifier = modifier) {
        // ExoPlayerを使用したストリーミング再生
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            PlayPauseButtonComponent(
                player = player,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            SeekBarComponent(
                modifier = Modifier
                    .fillMaxWidth(),
                player = player,
            )
        }
        PitchControllerComponent(
            currentKey = currentKey,
            onPitchUp = onPitchUp,
            onPitchDown = onPitchDown
        )
    }
}

private fun updatePitch(player: ExoPlayer?, key: PitchKey) {
    player?.playbackParameters = PlaybackParameters(
        1.0f,
        generatePitchFrequency(key)
    )
}

private fun generatePitchFrequency(key: PitchKey): Float {
    val tmpKey = key / 12
    return 2.0.pow(tmpKey.value).toFloat()
}

@Preview(showBackground = true)
@Composable
private fun PreviewPlayerSection() {
    PlayerSection(
        streamingUrl = "https://example.com/stream.m3u8",
        currentKey = PitchKey(0.0),
        onPitchUp = {},
        onPitchDown = {},
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    )
}