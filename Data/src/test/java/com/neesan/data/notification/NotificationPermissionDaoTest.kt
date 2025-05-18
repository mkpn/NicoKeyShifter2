package com.neesan.data.notification

import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import javax.inject.Inject

@HiltAndroidTest
@Config(application = HiltTestApplication::class)
@Suppress("NonAsciiCharacters", "TestFunctionName")
@RunWith(AndroidJUnit4::class)
class NotificationPermissionDaoTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var notificationPermissionDao: NotificationPermissionDao

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun 通知許可状態が未設定の場合はfalseが返されること() {
        // 初期状態ではfalseが返されることを確認
        assertFalse(notificationPermissionDao.isNotificationPermissionRequested())
    }

    @Test
    fun 通知許可状態を保存して取得できること() {
        // Arrange & Act
        notificationPermissionDao.setNotificationPermissionRequested()

        // Assert
        // 保存した状態が取得できることを確認
        assertTrue(notificationPermissionDao.isNotificationPermissionRequested())
    }

}