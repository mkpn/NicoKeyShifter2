package com.neesan.domain.favorite

import com.neesan.data.favorite.FavoriteRepository
import com.neesan.data.favorite.FavoriteVideoEntity
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

@Suppress("NonAsciiCharacters", "TestFunctionName")
class RemoveFavoriteVideoUseCaseTest {

    private lateinit var favoriteRepository: FavoriteRepository
    private lateinit var removeFavoriteVideoUseCase: RemoveFavoriteVideoUseCase

    @Before
    fun setup() {
        favoriteRepository = mock()
        removeFavoriteVideoUseCase = RemoveFavoriteVideoUseCase(favoriteRepository)
    }

    @Test
    fun お気に入り動画が削除されること() = runTest {
        // テストデータ
        val favoriteVideoDomainData = FavoriteVideoDomainData(
            videoId = "sm12345",
            title = "テスト動画",
            thumbnailUrl = "test_url",
            createdAt = 1000L
        )

        val expectedEntity = FavoriteVideoEntity(
            videoId = "sm12345",
            title = "テスト動画",
            thumbnailUrl = "test_url",
            createdAt = 1000L
        )

        // テスト実行
        removeFavoriteVideoUseCase.invoke(favoriteVideoDomainData)

        // 検証
        verify(favoriteRepository).removeFavoriteVideo(expectedEntity)
    }
}