package com.neesan.domain.favorite

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.neesan.core.runWithDescription
import com.neesan.data.favorite.FavoriteRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
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
class GetFavoriteVideoByIdUseCaseTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var favoriteRepository: FavoriteRepository

    @Inject
    lateinit var getFavoriteVideoByIdUseCase: GetFavoriteVideoByIdUseCase

    @Inject
    lateinit var addFavoriteVideoUseCase: AddFavoriteVideoUseCase

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun 指定したIDのお気に入り動画が取得できること() = runWithDescription {
        runTest {
            // テストデータ
            val favoriteVideoDomainData = FavoriteVideoDomainData(
                videoId = "sm12345",
                title = "テスト動画",
                thumbnailUrl = "test_url",
                createdAt = 1000L
            )

            // お気に入りに追加
            addFavoriteVideoUseCase.invoke(favoriteVideoDomainData)

            // テスト実行
            val result = getFavoriteVideoByIdUseCase.invoke("sm12345").first()

            // 検証
            assertEquals("sm12345", result?.videoId)
            assertEquals("テスト動画", result?.title)
            assertEquals("test_url", result?.thumbnailUrl)
            assertEquals(1000L, result?.createdAt)
        }
    }

    @Test
    fun 存在しないIDの場合nullが返ること() = runWithDescription {
        runTest {
            // テスト実行
            val result = getFavoriteVideoByIdUseCase.invoke("not_exist").first()

            // 検証
            assertNull(result)
        }
    }
}