package com.neesan.domain.favorite

import com.neesan.data.favorite.FavoriteRepository
import com.neesan.domain.favorite.FavoriteVideoMapper.toEntity
import javax.inject.Inject

class AddFavoriteVideoUseCase @Inject constructor(
    private val favoriteRepository: FavoriteRepository
) {
    suspend operator fun invoke(favoriteVideo: FavoriteVideoDomainData) {
        favoriteRepository.addFavoriteVideo(favoriteVideo.toEntity())
    }
}