package com.neesan.domain.favorite

import com.neesan.data.favorite.FavoriteRepository
import javax.inject.Inject

class RemoveFavoriteVideoByIdUseCase @Inject constructor(
    private val favoriteRepository: FavoriteRepository
) {
    suspend fun invoke(videoId: String) {
        favoriteRepository.removeFavoriteVideoById(videoId)
    }
}