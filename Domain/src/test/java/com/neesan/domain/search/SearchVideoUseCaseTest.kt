package com.neesan.domain.search

import com.neesan.data.favorite.FavoriteRepository
import com.neesan.data.favorite.FavoriteVideoEntity
import com.neesan.data.search.SearchRepository
import com.neesan.data.search.SearchVideoResponse
import com.neesan.data.search.Video
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
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

        // お気に入り一覧は空
        whenever(favoriteRepository.getAllFavoriteVideos()).thenReturn(flowOf(emptyList()))

        // テスト実行
        val result = searchVideoUseCase.invoke("テスト").first()

        // 検証
        assertEquals(2, result.size)
        assertEquals("1", result[0].id)
        assertEquals("テスト動画1", result[0].title)
        assertEquals(1000, result[0].viewCount)
        assertEquals("url1", result[0].thumbnailUrl)
        assertFalse(result[0].isFavorite)
        assertFalse(result[1].isFavorite)
    }

    @Test
    fun お気に入り済みの動画はisFavoriteがtrueになること() = runTest {
        val mockVideos = listOf(
            Video(contentId = "1", title = "テスト動画1", viewCounter = 1000, thumbnailUrl = "url1"),
            Video(contentId = "2", title = "テスト動画2", viewCounter = 2000, thumbnailUrl = "url2")
        )

        whenever(searchRepository.searchVideos("テスト", "title", "-viewCounter", 100))
            .thenReturn(flowOf(SearchVideoResponse(mockVideos)))

        whenever(favoriteRepository.getAllFavoriteVideos()).thenReturn(
            flowOf(
                listOf(
                    FavoriteVideoEntity(videoId = "1", title = "テスト動画1", thumbnailUrl = "url1")
                )
            )
        )

        val result = searchVideoUseCase.invoke("テスト").first()

        assertEquals(2, result.size)
        assertTrue(result[0].isFavorite)
        assertFalse(result[1].isFavorite)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun お気に入りFlowの更新が検索結果のisFavoriteに反映されること() = runTest {
        val mockVideos = listOf(
            Video(contentId = "1", title = "テスト動画1", viewCounter = 1000, thumbnailUrl = "url1")
        )

        whenever(searchRepository.searchVideos("テスト", "title", "-viewCounter", 100))
            .thenReturn(flowOf(SearchVideoResponse(mockVideos)))

        val favoritesFlow = MutableStateFlow<List<FavoriteVideoEntity>>(
            listOf(FavoriteVideoEntity(videoId = "1", title = "テスト動画1", thumbnailUrl = "url1"))
        )
        whenever(favoriteRepository.getAllFavoriteVideos()).thenReturn(favoritesFlow)

        val emissions = mutableListOf<List<VideoDomainModel>>()
        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            searchVideoUseCase.invoke("テスト").collect { emissions.add(it) }
        }

        // 初回: お気に入り済み
        assertEquals(1, emissions.size)
        assertTrue(emissions[0][0].isFavorite)

        // お気に入りから削除されると再emitされ、isFavoriteがfalseになる
        favoritesFlow.value = emptyList()
        assertEquals(2, emissions.size)
        assertFalse(emissions[1][0].isFavorite)

        job.cancel()
    }
}
