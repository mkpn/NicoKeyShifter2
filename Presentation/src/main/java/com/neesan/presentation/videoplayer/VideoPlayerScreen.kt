package com.neesan.presentation.videoplayer

import android.util.Base64
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView

/**
 * 動画再生画面。現状はプレースホルダーとして動画タイトルを表示するのみ。
 */
@Composable
fun VideoPlayerScreen(destination: VideoPlayerDestination) {
    VideoPlayerContent(
        videoId = destination.videoId,
        title = destination.title
    )
}

@Composable
private fun VideoPlayerContent(videoId: String, title: String) {
    // 動画を埋め込む HTML を Base64 でエンコード
    val encodedHtml = remember(videoId) {
        val html = "<html><body><script type=\"application/javascript\" src=\"https://embed.nicovideo.jp/watch/$videoId/script?w=320&h=180\"></script></body></html>"
        Base64.encodeToString(html.toByteArray(), Base64.NO_PADDING)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // 現状はタイトルのみ表示
        Text(text = "動画ID: $videoId\nタイトル: $title")
        AndroidView(
            factory = {
                WebView(it).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    settings.javaScriptEnabled = true
                    // パフォーマンス向上のためキャッシュ設定などを適宜追加可能
                    webViewClient = WebViewClient()
                }
            },
            update = { webView ->
                webView.loadData(encodedHtml, "text/html", "base64")
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewVideoPlayerContent() {
    VideoPlayerContent(videoId = "sm12345678", title = "サンプル動画")
}
