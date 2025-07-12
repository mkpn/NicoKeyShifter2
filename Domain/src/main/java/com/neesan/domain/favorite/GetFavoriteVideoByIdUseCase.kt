package com.neesan.domain.favorite

import com.neesan.data.favorite.FavoriteRepository
import com.neesan.domain.favorite.FavoriteVideoMapper.toDomainData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetFavoriteVideoByIdUseCase @Inject constructor(
    private val favoriteRepository: FavoriteRepository
) {
    fun invoke(videoId: String): Flow<FavoriteVideoDomainData?> {
        return favoriteRepository.getFavoriteVideoById(videoId)
            .map { it?.toDomainData() }
    }
}