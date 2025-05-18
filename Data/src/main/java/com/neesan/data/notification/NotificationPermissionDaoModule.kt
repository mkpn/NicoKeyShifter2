package com.neesan.data.notification

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NotificationPermissionDaoModule {

    @Provides
    @Singleton
    fun provideNotificationPermissionDao(
        @ApplicationContext context: Context,
    ): NotificationPermissionDao {
        val sharedPreferences = context.getSharedPreferences("notification_prefs", Context.MODE_PRIVATE)
        return NotificationPermissionDao(context, sharedPreferences)
    }
}