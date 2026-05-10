package com.neesan.domain.favorite

import com.neesan.data.favorite.FavoriteRepository
import com.neesan.domain.favorite.FavoriteVideoMapper.toEntity
import kotlinx.coroutines.flow.first
import javax.inject.Inject

/**
 * お気に入り動画を新規追加または更新する UseCase。
 * 既存レコードがある場合は createdAt を保持し、それ以外のフィールドのみ更新する。
 */
class AddOrUpdateFavoriteVideoUseCase @Inject constructor(
    private val favoriteRepository: FavoriteRepository
) {
    suspend fun invoke(favoriteVideo: FavoriteVideoDomainData) {
        val existing = favoriteRepository.getFavoriteVideoById(favoriteVideo.videoId).first()
        val resolved = favoriteVideo.copy(
            createdAt = existing?.createdAt ?: favoriteVideo.createdAt
        )
        favoriteRepository.addFavoriteVideo(resolved.toEntity())
    }
}
