package com.neesan.data.favorite


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteVideoDao {
    @Query("SELECT * FROM favorite_videos")
    fun getAllFavoriteVideos(): Flow<List<FavoriteVideoEntity>>

    @Query("SELECT * FROM favorite_videos WHERE videoId = :videoId")
    suspend fun getFavoriteVideoById(videoId: String): FavoriteVideoEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteVideo(favoriteVideo: FavoriteVideoEntity)

    @Delete
    suspend fun deleteFavoriteVideo(favoriteVideo: FavoriteVideoEntity)

    @Query("DELETE FROM favorite_videos WHERE videoId = :videoId")
    suspend fun deleteFavoriteVideoById(videoId: String)

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_videos WHERE videoId = :videoId)")
    suspend fun isFavorite(videoId: String): Boolean
} 