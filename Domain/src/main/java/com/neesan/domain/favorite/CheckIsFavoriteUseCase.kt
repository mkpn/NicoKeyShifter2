package com.neesan.domain.favorite

import com.neesan.data.favorite.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CheckIsFavoriteUseCase @Inject constructor(
    private val favoriteRepository: FavoriteRepository
) {
    fun invoke(videoId: String): Flow<Boolean> {
        return favoriteRepository.isFavorite(videoId)
    }
}