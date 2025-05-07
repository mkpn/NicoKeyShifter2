package com.neesan.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.neesan.data.favorite.FavoriteVideoDao
import com.neesan.data.favorite.FavoriteVideoEntity

@Database(
    entities = [FavoriteVideoEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteVideoDao(): FavoriteVideoDao
} 