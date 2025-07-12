package com.neesan.domain.favorite

import com.neesan.data.favorite.FavoriteRepository
import com.neesan.data.favorite.FavoriteVideoEntity
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

@Suppress("NonAsciiCharacters", "TestFunctionName")
class AddFavoriteVideoUseCaseTest {

    private lateinit var favoriteRepository: FavoriteRepository
    private lateinit var addFavoriteVideoUseCase: AddFavoriteVideoUseCase

    @Before
    fun setup() {
        favoriteRepository = mock()
        addFavoriteVideoUseCase = AddFavoriteVideoUseCase(favoriteRepository)
    }

    @Test
    fun お気に入り動画が追加されること() = runTest {
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
        addFavoriteVideoUseCase.invoke(favoriteVideoDomainData)

        // 検証
        verify(favoriteRepository).addFavoriteVideo(expectedEntity)
    }
}