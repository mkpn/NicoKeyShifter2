package com.neesan.data.favorite

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoriteRepository @Inject constructor(
    private val favoriteVideoDao: FavoriteVideoDao,
    private val coroutineDispatcher: CoroutineDispatcher
) {
    fun getAllFavoriteVideos(): Flow<List<FavoriteVideoEntity>> =
        favoriteVideoDao.getAllFavoriteVideos().flowOn(coroutineDispatcher)

    fun getFavoriteVideoById(videoId: String): Flow<FavoriteVideoEntity?> = flow {
        emit(favoriteVideoDao.getFavoriteVideoById(videoId))
    }.flowOn(coroutineDispatcher)

    suspend fun addFavoriteVideo(favoriteVideo: FavoriteVideoEntity) =
        withContext(coroutineDispatcher) {
            favoriteVideoDao.insertFavoriteVideo(favoriteVideo)
        }

    suspend fun removeFavoriteVideo(favoriteVideo: FavoriteVideoEntity) =
        withContext(coroutineDispatcher) {
            favoriteVideoDao.deleteFavoriteVideo(favoriteVideo)
        }

    suspend fun removeFavoriteVideoById(videoId: String) =
        withContext(coroutineDispatcher) {
            favoriteVideoDao.deleteFavoriteVideoById(videoId)
        }

    fun isFavorite(videoId: String): Flow<Boolean> = flow {
        emit(favoriteVideoDao.isFavorite(videoId))
    }.flowOn(coroutineDispatcher)
} 