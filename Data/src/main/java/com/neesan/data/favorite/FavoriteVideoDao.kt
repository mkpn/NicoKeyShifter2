package com.neesan.data.favorite


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteVideoDao {
    @Query("SELECT * FROM favorite_videos ORDER BY createdAt DESC")
    fun getAllFavoriteVideos(): Flow<List<FavoriteVideoEntity>>

    @Query("SELECT * FROM favorite_videos WHERE videoId = :videoId")
    suspend fun getFavoriteVideoById(videoId: String): FavoriteVideoEntity?

    @Upsert
    suspend fun upsertFavoriteVideo(favoriteVideo: FavoriteVideoEntity)

    @Delete
    suspend fun deleteFavoriteVideo(favoriteVideo: FavoriteVideoEntity)

    @Query("DELETE FROM favorite_videos WHERE videoId = :videoId")
    suspend fun deleteFavoriteVideoById(videoId: String)

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_videos WHERE videoId = :videoId)")
    suspend fun isFavorite(videoId: String): Boolean
} 