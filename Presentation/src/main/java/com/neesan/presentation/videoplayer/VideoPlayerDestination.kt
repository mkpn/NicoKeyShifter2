package com.neesan.presentation.videoplayer

import kotlinx.serialization.Serializable

/**
 * 動画再生画面の遷移に使用する型安全ルート。
 * 必要最小限として動画 ID とタイトル、サムネイル URL、
 * お気に入り状態のみを渡すようにしています。
 */
@Serializable
data class VideoPlayerDestination(
    val videoId: String,
    val title: String,
    val thumbnailUrl: String = "",
    val isFavorite: Boolean = false
) 