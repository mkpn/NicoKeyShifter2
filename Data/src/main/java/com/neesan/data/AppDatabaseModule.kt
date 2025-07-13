package com.neesan.data

import android.content.Context
import androidx.room.Room
import com.neesan.core.isRunningOnTest
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppDatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        if (isRunningOnTest()) {
            return Room.inMemoryDatabaseBuilder(
                context,
                AppDatabase::class.java,
            ).allowMainThreadQueries().build()
        } else {
            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "nico_key_shifter.db"
            ).build()
        }
    }
} 