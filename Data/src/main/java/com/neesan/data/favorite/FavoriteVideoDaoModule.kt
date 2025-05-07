package com.neesan.data.favorite

import com.neesan.data.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FavoriteVideoDaoModule {

    @Provides
    @Singleton
    fun provideFavoriteVideoDao(
        database: AppDatabase
    ) = database.favoriteVideoDao()
} 