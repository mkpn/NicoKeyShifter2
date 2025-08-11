package com.neesan.domain.favorite

import com.neesan.core.valueClass.PitchKey

/**
 * お気に入り動画のドメインデータ
 */
data class FavoriteVideoDomainData(
    val videoId: String,
    val title: String,
    val thumbnailUrl: String,
    val createdAt: Long,
    val keyValue: PitchKey = PitchKey(0.0)
){
    companion object {
        fun ofDefault() = FavoriteVideoDomainData(
            videoId = "sm12345678",
            title = "サンプルお気に入り動画",
            thumbnailUrl = "",
            createdAt = System.currentTimeMillis(),
            keyValue = PitchKey(0.0)
        )
    }
}
