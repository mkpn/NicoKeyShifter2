package com.neesan.presentation.player

import android.util.Base64
import android.util.Log
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.ui.compose.PlayerSurface
import androidx.media3.ui.compose.SURFACE_TYPE_SURFACE_VIEW
import androidx.media3.common.util.UnstableApi
import androidx.compose.foundation.clickable
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
    var showExoPlayer by remember { mutableStateOf(false) }
    var showControls by remember { mutableStateOf(true) }
    val context = LocalContext.current
    
    // ExoPlayerインスタンス
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build()
    }
    
    // プレイヤーのクリーンアップ
    DisposableEffect(exoPlayer) {
        onDispose {
            exoPlayer.release()
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
            exoPlayer.setMediaSource(hlsMediaSource)
            exoPlayer.prepare()
            showExoPlayer = true
        }
    }
    
    // 動画を埋め込む HTML を Base64 でエンコード
    val encodedHtml = remember(videoId) {
        val html = "<html><body><script type=\"application/javascript\" src=\"https://embed.nicovideo.jp/watch/$videoId/script?w=320&h=180\"></script></body></html>"
        Base64.encodeToString(html.toByteArray(), Base64.NO_PADDING)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "動画ID: $videoId\nタイトル: $title",
            modifier = Modifier.padding(16.dp)
        )
        
        if (showExoPlayer && streamingUrl != null) {
            // ExoPlayerを使用したストリーミング再生
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .padding(16.dp)
            ) {
                PlayerSurface(
                    player = exoPlayer,
                    surfaceType = SURFACE_TYPE_SURFACE_VIEW,
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { showControls = !showControls }
                )
            }
            
            // プレイヤーコントロール（表示/非表示切替可能）
            if (showControls) {
                PlayerControls(
                    isPlaying = exoPlayer.isPlaying,
                    onPlayPause = {
                        if (exoPlayer.isPlaying) {
                            exoPlayer.pause()
                        } else {
                            exoPlayer.play()
                        }
                    },
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
        
        // WebViewで音声URLを検出
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

@Composable
private fun PlayerControls(
    isPlaying: Boolean,
    onPlayPause: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onPlayPause,
            modifier = Modifier
                .size(64.dp)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = androidx.compose.foundation.shape.CircleShape
                )
        ) {
            Icon(
                painter = painterResource(
                    id = if (isPlaying) android.R.drawable.ic_media_pause else android.R.drawable.ic_media_play
                ),
                contentDescription = if (isPlaying) "Pause" else "Play",
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewVideoPlayerContent() {
    VideoPlayerContent(videoId = "sm12345678", title = "サンプル動画")
}
