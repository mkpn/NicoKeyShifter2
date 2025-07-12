package com.neesan.domain.favorite

import com.neesan.data.favorite.FavoriteRepository
import com.neesan.domain.favorite.FavoriteVideoMapper.toDomainData
import javax.inject.Inject

class GetFavoriteVideoByIdUseCase @Inject constructor(
    private val favoriteRepository: FavoriteRepository
) {
    suspend fun invoke(videoId: String): FavoriteVideoDomainData? {
        return favoriteRepository.getFavoriteVideoById(videoId)?.toDomainData()
    }
}