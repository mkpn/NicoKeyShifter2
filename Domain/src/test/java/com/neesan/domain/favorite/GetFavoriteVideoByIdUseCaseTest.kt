package com.neesan.domain.favorite

import com.neesan.data.favorite.FavoriteRepository
import com.neesan.data.favorite.FavoriteVideoEntity
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@Suppress("NonAsciiCharacters", "TestFunctionName")
class GetFavoriteVideoByIdUseCaseTest {

    private lateinit var favoriteRepository: FavoriteRepository
    private lateinit var getFavoriteVideoByIdUseCase: GetFavoriteVideoByIdUseCase

    @Before
    fun setup() {
        favoriteRepository = mock()
        getFavoriteVideoByIdUseCase = GetFavoriteVideoByIdUseCase(favoriteRepository)
    }

    @Test
    fun 指定したIDのお気に入り動画が取得できること() = runTest {
        // モックデータ
        val mockEntity = FavoriteVideoEntity(
            videoId = "sm12345",
            title = "テスト動画",
            thumbnailUrl = "test_url",
            createdAt = 1000L
        )
        
        // モックの振る舞いを設定
        whenever(favoriteRepository.getFavoriteVideoById("sm12345"))
            .thenReturn(mockEntity)

        // テスト実行
        val result = getFavoriteVideoByIdUseCase.invoke("sm12345")

        // 検証
        assertEquals("sm12345", result?.videoId)
        assertEquals("テスト動画", result?.title)
        assertEquals("test_url", result?.thumbnailUrl)
        assertEquals(1000L, result?.createdAt)
    }

    @Test
    fun 存在しないIDの場合nullが返ること() = runTest {
        // モックの振る舞いを設定
        whenever(favoriteRepository.getFavoriteVideoById("not_exist"))
            .thenReturn(null)

        // テスト実行
        val result = getFavoriteVideoByIdUseCase.invoke("not_exist")

        // 検証
        assertNull(result)
    }
}