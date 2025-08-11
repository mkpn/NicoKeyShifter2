package com.neesan.domain.search

import com.neesan.data.favorite.FavoriteRepository
import com.neesan.data.search.SearchRepository
import com.neesan.data.search.SearchVideoResponse
import com.neesan.data.search.Video
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@Suppress("NonAsciiCharacters", "TestFunctionName")
class SearchVideoUseCaseTest {

    private lateinit var searchRepository: SearchRepository
    private lateinit var favoriteRepository: FavoriteRepository
    private lateinit var searchVideoUseCase: SearchVideoUseCase

    @Before
    fun setup() {
        searchRepository = mock()
        favoriteRepository = mock()
        searchVideoUseCase = SearchVideoUseCase(searchRepository, favoriteRepository)
    }

    @Test
    fun 検索結果が正常に取得できること() = runTest {
        // モックデータ
        val mockVideos = listOf(
            Video(contentId = "1", title = "テスト動画1", viewCounter = 1000, thumbnailUrl = "url1"),
            Video(contentId = "2", title = "テスト動画2", viewCounter = 2000, thumbnailUrl = "url2")
        )
        
        // モックの振る舞いを設定
        whenever(searchRepository.searchVideos("テスト", "title", "-viewCounter", 100))
            .thenReturn(flow { 
                emit(SearchVideoResponse(mockVideos)) 
            })
        
        // お気に入り状態のモック設定（すべてfalse）
        whenever(favoriteRepository.isFavorite("1")).thenReturn(flow { emit(false) })
        whenever(favoriteRepository.isFavorite("2")).thenReturn(flow { emit(false) })

        // テスト実行
        val result = searchVideoUseCase.invoke("テスト").first()

        // 検証
        assertEquals(2, result.size)
        assertEquals("1", result[0].id)
        assertEquals("テスト動画1", result[0].title)
        assertEquals(1000, result[0].viewCount)
        assertEquals("url1", result[0].thumbnailUrl)
    }
}
