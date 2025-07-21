package com.neesan.presentation.videoplayer

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.neesan.domain.search.VideoDomainModel

/**
 * 動画再生画面。現状はプレースホルダーとして動画タイトルを表示するのみ。
 */
@Composable
fun VideoPlayerScreen(video: VideoDomainModel?) {
    Text(text = "動画再生画面: ${video?.title ?: "No video"}")
}

@Preview(showBackground = true)
@Composable
private fun PreviewVideoPlayerScreen() {
    VideoPlayerScreen(VideoDomainModel.ofDefault())
} 