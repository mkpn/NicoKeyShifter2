package com.neesan.data.favorite

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_videos")
data class FavoriteVideoEntity(
    @PrimaryKey
    val videoId: String,
    val title: String,
    val thumbnailUrl: String,
    val createdAt: Long = System.currentTimeMillis()
) 