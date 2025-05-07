package com.neesan.data.favorite

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoriteRepository @Inject constructor(
    private val favoriteVideoDao: FavoriteVideoDao,
    private val coroutineDispatcher: CoroutineDispatcher
) {
    fun getAllFavoriteVideos(): Flow<List<FavoriteVideoEntity>> =
        favoriteVideoDao.getAllFavoriteVideos().flowOn(coroutineDispatcher)

    suspend fun getFavoriteVideoById(videoId: String): FavoriteVideoEntity? =
        favoriteVideoDao.getFavoriteVideoById(videoId)

    suspend fun addFavoriteVideo(favoriteVideo: FavoriteVideoEntity) =
        favoriteVideoDao.insertFavoriteVideo(favoriteVideo)

    suspend fun removeFavoriteVideo(favoriteVideo: FavoriteVideoEntity) =
        favoriteVideoDao.deleteFavoriteVideo(favoriteVideo)

    suspend fun removeFavoriteVideoById(videoId: String) =
        favoriteVideoDao.deleteFavoriteVideoById(videoId)

    suspend fun isFavorite(videoId: String): Boolean =
        favoriteVideoDao.isFavorite(videoId)
} 