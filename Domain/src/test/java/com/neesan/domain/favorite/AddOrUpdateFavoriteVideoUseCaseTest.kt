package com.neesan.domain.favorite

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.neesan.core.runWithDescription
import com.neesan.core.valueClass.PitchKey
import com.neesan.data.favorite.FavoriteRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import javax.inject.Inject

@HiltAndroidTest
@Config(application = HiltTestApplication::class)
@RunWith(AndroidJUnit4::class)
@Suppress("NonAsciiCharacters", "TestFunctionName")
class AddOrUpdateFavoriteVideoUseCaseTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var favoriteRepository: FavoriteRepository

    @Inject
    lateinit var addOrUpdateFavoriteVideoUseCase: AddOrUpdateFavoriteVideoUseCase

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun 未登録の場合は指定した値で新規追加されること() = runWithDescription {
        runTest {
            val newFavorite = FavoriteVideoDomainData(
                videoId = "sm_new",
                title = "新規動画",
                thumbnailUrl = "new_url",
                createdAt = 1000L,
                keyValue = PitchKey(2.0)
            )

            addOrUpdateFavoriteVideoUseCase.invoke(newFavorite)

            val result = favoriteRepository.getFavoriteVideoById("sm_new").first()
            assertEquals("sm_new", result?.videoId)
            assertEquals("新規動画", result?.title)
            assertEquals("new_url", result?.thumbnailUrl)
            assertEquals(1000L, result?.createdAt)
            assertEquals(PitchKey(2.0), result?.keyValue)
        }
    }

    @Test
    fun 登録済みのキー値を更新してもcreatedAtが保持されること() = runWithDescription {
        runTest {
            // 既存お気に入りとして登録
            val initial = FavoriteVideoDomainData(
                videoId = "sm12345",
                title = "テスト動画",
                thumbnailUrl = "test_url",
                createdAt = 1000L,
                keyValue = PitchKey(0.0)
            )
            addOrUpdateFavoriteVideoUseCase.invoke(initial)

            // 別の createdAt と新しい keyValue で更新
            val updated = initial.copy(
                createdAt = 9999L,
                keyValue = PitchKey(3.0)
            )
            addOrUpdateFavoriteVideoUseCase.invoke(updated)

            val result = favoriteRepository.getFavoriteVideoById("sm12345").first()
            // createdAt は初回登録時の値が保持される
            assertEquals(1000L, result?.createdAt)
            // keyValue は更新後の値が反映される
            assertEquals(PitchKey(3.0), result?.keyValue)
            assertEquals("テスト動画", result?.title)
        }
    }
}
