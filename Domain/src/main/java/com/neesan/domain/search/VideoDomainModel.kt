package com.neesan.domain.search

/**
 * 動画のドメインモデル
 */
data class VideoDomainModel(
    val id: String,
    val title: String,
    val viewCount: Int,
    val thumbnailUrl: String,
    val isFavorite: Boolean = false
) {
    companion object {
        fun ofDefault() = VideoDomainModel(
            id = "sm12345678",
            title = "サンプル動画タイトル",
            viewCount = 12345,
            thumbnailUrl = ""
        )
    }
}
