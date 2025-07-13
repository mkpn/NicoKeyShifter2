package com.neesan.domain.favorite

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.neesan.core.runWithDescription
import com.neesan.data.favorite.FavoriteRepository
import com.neesan.data.favorite.FavoriteVideoEntity
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
class GetAllFavoriteVideosUseCaseTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var favoriteRepository: FavoriteRepository

    @Inject
    lateinit var getAllFavoriteVideosUseCase: GetAllFavoriteVideosUseCase

    @Inject
    lateinit var addFavoriteVideoUseCase: AddFavoriteVideoUseCase

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun お気に入り動画が全件取得できること() = runWithDescription {
        runTest {
            // テストデータ
            val favoriteVideo1 = FavoriteVideoDomainData(
                videoId = "sm1",
                title = "お気に入り動画1",
                thumbnailUrl = "url1",
                createdAt = 1000L
            )
            val favoriteVideo2 = FavoriteVideoDomainData(
                videoId = "sm2",
                title = "お気に入り動画2",
                thumbnailUrl = "url2",
                createdAt = 2000L
            )

            // お気に入りに追加
            addFavoriteVideoUseCase.invoke(favoriteVideo1)
            addFavoriteVideoUseCase.invoke(favoriteVideo2)

            // テスト実行
            val result = getAllFavoriteVideosUseCase.invoke().first()

            // 検証
            assertEquals(2, result.size)
            assertEquals("sm1", result[0].videoId)
            assertEquals("お気に入り動画1", result[0].title)
            assertEquals("url1", result[0].thumbnailUrl)
            assertEquals(1000L, result[0].createdAt)
            assertEquals("sm2", result[1].videoId)
            assertEquals("お気に入り動画2", result[1].title)
            assertEquals("url2", result[1].thumbnailUrl)
            assertEquals(2000L, result[1].createdAt)
        }
    }

    @Test
    fun お気に入り動画が空の場合空のリストが返ること() = runWithDescription {
        runTest {
            // テスト実行
            val result = getAllFavoriteVideosUseCase.invoke().first()

            // 検証
            assertEquals(0, result.size)
        }
    }
}