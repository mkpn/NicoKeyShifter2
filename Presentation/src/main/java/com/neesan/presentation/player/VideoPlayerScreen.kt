package com.neesan.presentation.player

import android.util.Base64
import android.util.Log
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.util.UnstableApi
import androidx.compose.foundation.layout.Box
import com.neesan.presentation.isPreview
import com.neesan.presentation.player.section.PlayerSection
import kotlinx.serialization.Serializable

/**
 * 動画再生画面の遷移に使用する型安全ルート。
 * 必要最小限として動画 ID とタイトル、サムネイル URL、
 * お気に入り状態のみを渡すようにしています。
 */
@Serializable
data class PlayerDestination(
    val videoId: String,
    val title: String,
    val thumbnailUrl: String = "",
    val isFavorite: Boolean = false
)

/**
 * 音声リソース監視用のカスタムWebViewClient
 * ニコニコ動画の音声ファイルのみを検出
 */
private class AudioResourceMonitoringWebViewClient(
    private val onAudioResourceDetected: (String) -> Unit
) : WebViewClient() {

    companion object {
        private const val TAG = "AudioResourceMonitor"
    }

    override fun shouldInterceptRequest(
        view: WebView?,
        request: WebResourceRequest?
    ): WebResourceResponse? {
        request?.url?.toString()?.let { url ->
            // 音声リソースかどうかを判定
            if (isAudioResource(url)) {
                Log.i(TAG, "Audio resource detected: $url")
                onAudioResourceDetected(url)
            }
        }

        // リクエストを通常通り処理するためnullを返す
        return super.shouldInterceptRequest(view, request)
    }

    /**
     * 音声リソースかどうかを判定
     * ニコニコ動画の音声ファイルに特化した判定
     */
    private fun isAudioResource(url: String): Boolean {
        return url.contains("audio-aac") && url.contains(".m3u8")
    }
}

/**
 * 動画再生画面。音声リソースの検出とURLの表示機能付き
 */
@Composable
fun VideoPlayerScreen(destination: PlayerDestination) {
    VideoPlayerContent(
        videoId = destination.videoId,
        title = destination.title
    )
}

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
private fun VideoPlayerContent(videoId: String, title: String) {
    // 検出された音声URLを保存するリスト
    val detectedAudioUrls = remember { mutableStateListOf<String>() }
    var streamingUrl by remember { mutableStateOf<String?>(null) }

    // 動画を埋め込む HTML を Base64 でエンコード
    val encodedHtml = remember(videoId) {
        val html =
            "<html><body><script type=\"application/javascript\" src=\"https://embed.nicovideo.jp/watch/$videoId/script?w=320&h=180\"></script></body></html>"
        Base64.encodeToString(html.toByteArray(), Base64.NO_PADDING)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "動画ID: $videoId\nタイトル: $title",
            modifier = Modifier.padding(16.dp)
        )

        // WebViewで音声URLを検出
        if (!isPreview()) {
            AndroidView(
                modifier = Modifier.weight(1f),
                factory = {
                    WebView(it).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        settings.javaScriptEnabled = true
                        settings.domStorageEnabled = true
                        settings.allowFileAccess = true
                        settings.allowContentAccess = true
                        settings.mediaPlaybackRequiresUserGesture = false

                        // カスタムWebViewClientを設定
                        webViewClient = AudioResourceMonitoringWebViewClient { url ->
                            Log.i("VideoPlayer", "Audio resource detected: $url")
                            detectedAudioUrls.add(url)
                            // 最初に検出されたURLをストリーミング用に使用
                            if (streamingUrl == null) {
                                streamingUrl = url
                            }
                        }
                    }
                },
                update = { webView ->
                    webView.loadData(encodedHtml, "text/html", "base64")
                }
            )
        }

            if (streamingUrl != null) {
                // ExoPlayerを使用したストリーミング再生
                PlayerSection(
                    streamingUrl = streamingUrl,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .padding(16.dp)
                )
            }

            // 検出されたURLの表示（デバッグ用）
            if (detectedAudioUrls.isNotEmpty()) {
                Text(
                    text = "検出されたストリーミングURL: ${detectedAudioUrls.size}個",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    private fun PreviewVideoPlayerContent() {
        VideoPlayerContent(videoId = "sm12345678", title = "サンプル動画")
    }
