package com.neesan.domain.notification

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
class UpdateNotificationPermissionRequestedUseCaseTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var checkNotificationPermissionRequestedUseCase: CheckNotificationPermissionRequestedUseCase

    @Inject
    lateinit var updateNotificationPermissionRequestedUseCase: UpdateNotificationPermissionRequestedUseCase

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun 通知許可をリクエストしたかのフラグをtrueに更新できる() {
        // Arrange
        // 通知許可をリクエスト済みに設定する
        val beforeUpdate = checkNotificationPermissionRequestedUseCase.invoke()
        assertFalse(beforeUpdate)

        updateNotificationPermissionRequestedUseCase.invoke()
        val afterUpdate = checkNotificationPermissionRequestedUseCase.invoke()
        // Assert
        assertTrue(afterUpdate)
    }
} 