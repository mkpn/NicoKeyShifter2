package com.neesan.domain.search

import java.io.Serializable

/**
 * 動画のドメインモデル
 */
data class VideoDomainModel(
    val id: String,
    val title: String,
    val viewCount: Int,
    val thumbnailUrl: String,
    val isFavorite: Boolean = false
) : Serializable {
    companion object {
        fun ofDefault() = VideoDomainModel(
            id = "sm12345678",
            title = "サンプル動画タイトル",
            viewCount = 12345,
            thumbnailUrl = ""
        )
    }
}
