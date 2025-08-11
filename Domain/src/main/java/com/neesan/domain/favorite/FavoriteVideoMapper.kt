package com.neesan.domain.favorite

import com.neesan.data.favorite.FavoriteVideoEntity

object FavoriteVideoMapper {
    fun FavoriteVideoEntity.toDomainData(): FavoriteVideoDomainData {
        return FavoriteVideoDomainData(
            videoId = videoId,
            title = title,
            thumbnailUrl = thumbnailUrl,
            createdAt = createdAt,
            keyValue = keyValue
        )
    }

    fun FavoriteVideoDomainData.toEntity(): FavoriteVideoEntity {
        return FavoriteVideoEntity(
            videoId = videoId,
            title = title,
            thumbnailUrl = thumbnailUrl,
            createdAt = createdAt,
            keyValue = keyValue
        )
    }
}
