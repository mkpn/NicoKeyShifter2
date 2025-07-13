package com.neesan.domain.favorite

import com.neesan.data.favorite.FavoriteRepository
import com.neesan.domain.favorite.FavoriteVideoMapper.toDomainData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAllFavoriteVideosUseCase @Inject constructor(
    private val favoriteRepository: FavoriteRepository
) {
    fun invoke(): Flow<List<FavoriteVideoDomainData>> {
        return favoriteRepository.getAllFavoriteVideos()
            .map { entityList ->
                entityList.map { it.toDomainData() }
            }
    }
}