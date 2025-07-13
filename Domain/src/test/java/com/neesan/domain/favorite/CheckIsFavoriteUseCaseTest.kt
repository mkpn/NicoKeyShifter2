package com.neesan.domain.favorite

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.neesan.core.runWithDescription
import com.neesan.data.favorite.FavoriteRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
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
@RunWith(AndroidJUnit4::class)
@Suppress("NonAsciiCharacters", "TestFunctionName")
class CheckIsFavoriteUseCaseTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var favoriteRepository: FavoriteRepository

    @Inject
    lateinit var checkIsFavoriteUseCase: CheckIsFavoriteUseCase

    @Inject
    lateinit var addFavoriteVideoUseCase: AddFavoriteVideoUseCase

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun お気に入りに登録済みの動画の場合trueが返ること() = runWithDescription {
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
            val result = checkIsFavoriteUseCase.invoke("sm12345").first()

            // 検証
            assertTrue(result)
        }
    }

    @Test
    fun お気に入りに未登録の動画の場合falseが返ること() = runWithDescription {
        runTest {
            // テスト実行
            val result = checkIsFavoriteUseCase.invoke("sm99999").first()

            // 検証
            assertFalse(result)
        }
    }
}