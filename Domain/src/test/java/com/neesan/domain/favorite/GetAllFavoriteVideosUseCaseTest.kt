package com.neesan.domain.favorite

import com.neesan.data.favorite.FavoriteRepository
import com.neesan.data.favorite.FavoriteVideoEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@Suppress("NonAsciiCharacters", "TestFunctionName")
class GetAllFavoriteVideosUseCaseTest {

    private lateinit var favoriteRepository: FavoriteRepository
    private lateinit var getAllFavoriteVideosUseCase: GetAllFavoriteVideosUseCase

    @Before
    fun setup() {
        favoriteRepository = mock()
        getAllFavoriteVideosUseCase = GetAllFavoriteVideosUseCase(favoriteRepository)
    }

    @Test
    fun お気に入り動画が全件取得できること() = runTest {
        // モックデータ
        val mockEntities = listOf(
            FavoriteVideoEntity(
                videoId = "sm1",
                title = "お気に入り動画1",
                thumbnailUrl = "url1",
                createdAt = 1000L
            ),
            FavoriteVideoEntity(
                videoId = "sm2",
                title = "お気に入り動画2",
                thumbnailUrl = "url2",
                createdAt = 2000L
            )
        )
        
        // モックの振る舞いを設定
        whenever(favoriteRepository.getAllFavoriteVideos())
            .thenReturn(flow { emit(mockEntities) })

        // テスト実行
        val result = getAllFavoriteVideosUseCase.invoke().first()

        // 検証
        assertEquals(2, result.size)
        assertEquals("sm1", result[0].videoId)
        assertEquals("お気に入り動画1", result[0].title)
        assertEquals("url1", result[0].thumbnailUrl)
        assertEquals(1000L, result[0].createdAt)
    }

    @Test
    fun お気に入り動画が空の場合空のリストが返ること() = runTest {
        // モックの振る舞いを設定
        whenever(favoriteRepository.getAllFavoriteVideos())
            .thenReturn(flow { emit(emptyList()) })

        // テスト実行
        val result = getAllFavoriteVideosUseCase.invoke().first()

        // 検証
        assertEquals(0, result.size)
    }
}