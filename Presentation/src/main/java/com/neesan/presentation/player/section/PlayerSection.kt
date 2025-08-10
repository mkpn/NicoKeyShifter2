package com.neesan.presentation.player.section

import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.ui.compose.PlayerSurface
import androidx.media3.ui.compose.SURFACE_TYPE_SURFACE_VIEW
import com.neesan.presentation.isPreview
import com.neesan.presentation.player.component.PlayPauseButtonComponent

@OptIn(UnstableApi::class)
@Composable
fun PlayerSection(
    streamingUrl: String?,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val isPreview = isPreview()
    val isPlaying by remember { mutableStateOf(false) }

    val exoPlayer = remember {
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
            exoPlayer?.setMediaSource(hlsMediaSource)
            exoPlayer?.prepare()
        }
    }

    // プレイヤーのクリーンアップ
    DisposableEffect(exoPlayer) {
        onDispose {
            exoPlayer?.release()
        }
    }

    // ExoPlayerを使用したストリーミング再生
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        PlayerSurface(
            player = exoPlayer,
            surfaceType = SURFACE_TYPE_SURFACE_VIEW,
            modifier = Modifier.fillMaxSize()
        )

        IconButton(
            onClick = {
                exoPlayer?.let {
                    if (isPlaying) {
                        it.pause()
                    } else {
                        it.play()
                    }
                }
            },
            modifier = Modifier
                .size(64.dp)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = androidx.compose.foundation.shape.CircleShape
                )
        ) {
            PlayPauseButtonComponent(
                player = exoPlayer,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewPlayerSection() {
    PlayerSection(
        streamingUrl = "https://example.com/stream.m3u8",
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .padding(16.dp)
    )
}