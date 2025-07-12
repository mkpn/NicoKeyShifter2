package com.neesan.domain.favorite

import com.neesan.data.favorite.FavoriteRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

@Suppress("NonAsciiCharacters", "TestFunctionName")
class RemoveFavoriteVideoByIdUseCaseTest {

    private lateinit var favoriteRepository: FavoriteRepository
    private lateinit var removeFavoriteVideoByIdUseCase: RemoveFavoriteVideoByIdUseCase

    @Before
    fun setup() {
        favoriteRepository = mock()
        removeFavoriteVideoByIdUseCase = RemoveFavoriteVideoByIdUseCase(favoriteRepository)
    }

    @Test
    fun 指定したIDのお気に入り動画が削除されること() = runTest {
        // テストデータ
        val videoId = "sm12345"

        // テスト実行
        removeFavoriteVideoByIdUseCase.invoke(videoId)

        // 検証
        verify(favoriteRepository).removeFavoriteVideoById(videoId)
    }
}