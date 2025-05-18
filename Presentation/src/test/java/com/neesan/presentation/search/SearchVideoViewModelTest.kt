package com.neesan.presentation.search

import com.neesan.core.exception.SearchException
import com.neesan.domain.notification.CheckNotificationPermissionRequestedUseCase
import com.neesan.domain.notification.UpdateNotificationPermissionRequestedUseCase
import com.neesan.domain.search.SearchVideoUseCase
import com.neesan.domain.search.VideoDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class SearchVideoViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var searchVideoUseCase: SearchVideoUseCase
    private lateinit var checkNotificationPermissionRequestedUseCase: CheckNotificationPermissionRequestedUseCase
    private lateinit var updateNotificationPermissionRequestedUseCase: UpdateNotificationPermissionRequestedUseCase
    private lateinit var viewModel: SearchVideoViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        searchVideoUseCase = mock()
        checkNotificationPermissionRequestedUseCase = mock()
        updateNotificationPermissionRequestedUseCase = mock()
        viewModel = SearchVideoViewModel(
            searchVideoUseCase,
            checkNotificationPermissionRequestedUseCase,
            updateNotificationPermissionRequestedUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `初期状態が正しいこと`() {
        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertTrue(state.videos.isEmpty())
        assertNull(state.errorMessage)
        assertEquals("", state.query)
        assertTrue(state.isInitialState)
        assertFalse(state.isError)
        assertFalse(state.isSuccess)
    }

    @Test
    fun `検索が成功した場合、UIStateが正しく更新されること`() = runTest {
        // モックデータ
        val videos = listOf(
            VideoDomainModel(id = "1", title = "テスト動画1", viewCount = 1000, thumbnailUrl = "url1"),
            VideoDomainModel(id = "2", title = "テスト動画2", viewCount = 2000, thumbnailUrl = "url2")
        )
        
        // モックの振る舞いを設定
        whenever(searchVideoUseCase.invoke("テスト", "title", "-viewCounter", 100))
            .thenReturn(flowOf(videos))

        // 検索実行
        viewModel.search("テスト")
        testDispatcher.scheduler.advanceUntilIdle()

        // 検証
        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals("テスト", state.query)
        assertEquals(2, state.videos.size)
        assertNull(state.errorMessage)
        assertFalse(state.isInitialState)
        assertFalse(state.isSearchResultEmpty)
        assertTrue(state.isSuccess)
    }

    @Test
    fun `検索結果が空の場合、UIStateが正しく更新されること`() = runTest {
        // モックの振る舞いを設定
        whenever(searchVideoUseCase.invoke("テスト", "title", "-viewCounter", 100))
            .thenReturn(flowOf(emptyList()))

        // 検索実行
        viewModel.search("テスト")
        testDispatcher.scheduler.advanceUntilIdle()

        // 検証
        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals("テスト", state.query)
        assertTrue(state.videos.isEmpty())
        assertNull(state.errorMessage)
        assertFalse(state.isInitialState)
        assertTrue(state.isSearchResultEmpty)
        assertTrue(state.isSuccess)
    }

    @Test
    fun `検索でエラーが発生した場合、UIStateが正しく更新されること`() = runTest {
        // モックの振る舞いを設定
        whenever(searchVideoUseCase.invoke("テスト", "title", "-viewCounter", 100))
            .thenReturn(flow { throw SearchException.ApiError(404) })

        // 検索実行
        viewModel.search("テスト")
        testDispatcher.scheduler.advanceUntilIdle()

        // 検証
        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals("テスト", state.query)
        assertTrue(state.videos.isEmpty())
        assertTrue(state.errorMessage?.contains("APIエラー") == true)
        assertTrue(state.errorMessage?.contains("404") == true)
        assertFalse(state.isInitialState)
        assertFalse(state.isSearchResultEmpty)
        assertTrue(state.isError)
    }

    @Test
    fun `clearSearchを実行すると初期状態に戻ること`() = runTest {
        // 検索実行
        whenever(searchVideoUseCase.invoke("テスト", "title", "-viewCounter", 100))
            .thenReturn(flowOf(listOf(VideoDomainModel(id = "1", title = "テスト", viewCount = 1000, thumbnailUrl = "url"))))
        
        viewModel.search("テスト")
        testDispatcher.scheduler.advanceUntilIdle()
        
        // クリア実行
        viewModel.clearSearch()
        
        // 検証
        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertTrue(state.videos.isEmpty())
        assertNull(state.errorMessage)
        assertEquals("", state.query)
        assertTrue(state.isInitialState)
    }
}
