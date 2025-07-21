package com.neesan.presentation.videoplayer

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

/**
 * 動画再生画面。現状はプレースホルダーとして動画タイトルを表示するのみ。
 */
@Composable
fun VideoPlayerScreen(destination: VideoPlayerDestination) {
    Text(text = "動画再生画面: ${destination.title}")
}

@Preview(showBackground = true)
@Composable
private fun PreviewVideoPlayerScreen() {
    VideoPlayerScreen(VideoPlayerDestination(videoId = "sm12345678", title = "サンプル動画", thumbnailUrl = ""))
} 