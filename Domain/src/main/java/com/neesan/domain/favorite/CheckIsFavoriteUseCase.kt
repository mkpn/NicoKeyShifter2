package com.neesan.domain.favorite

import com.neesan.data.favorite.FavoriteRepository
import javax.inject.Inject

class CheckIsFavoriteUseCase @Inject constructor(
    private val favoriteRepository: FavoriteRepository
) {
    suspend operator fun invoke(videoId: String): Boolean {
        return favoriteRepository.isFavorite(videoId)
    }
}