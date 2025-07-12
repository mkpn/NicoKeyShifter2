package com.neesan.domain.favorite

/**
 * お気に入り動画のドメインデータ
 */
data class FavoriteVideoDomainData(
    val videoId: String,
    val title: String,
    val thumbnailUrl: String,
    val createdAt: Long
) {
    companion object {
        fun ofDefault() = FavoriteVideoDomainData(
            videoId = "sm12345678",
            title = "サンプルお気に入り動画",
            thumbnailUrl = "",
            createdAt = System.currentTimeMillis()
        )
    }
}