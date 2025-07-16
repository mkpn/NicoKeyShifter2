package com.neesan.favorite

import com.neesan.core.runWithDescription
import com.neesan.domain.favorite.FavoriteVideoDomainData
import com.neesan.presentation.favorite.FavoriteVideoUiState
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

@Suppress("NonAsciiCharacters", "TestFunctionName")
class FavoriteVideoUiStateTest {

    @Test
    fun 初期状態でisFavoriteEmptyがtrueになること() = runWithDescription {
        // テストデータ
        val uiState = FavoriteVideoUiState()

        // 検証
        assertTrue(uiState.isFavoriteEmpty)
        assertFalse(uiState.isLoading)
        assertTrue(uiState.favoriteVideos.isEmpty())
        assertFalse(uiState.isError)
    }

    @Test
    fun ローディング中はisFavoriteEmptyがfalseになること() = runWithDescription {
        // テストデータ
        val uiState = FavoriteVideoUiState(
            isLoading = true,
            favoriteVideos = emptyList()
        )

        // 検証
        assertFalse(uiState.isFavoriteEmpty)
        assertTrue(uiState.isLoading)
    }

    @Test
    fun お気に入り動画がある場合isFavoriteEmptyがfalseになること() = runWithDescription {
        // テストデータ
        val favoriteVideo = FavoriteVideoDomainData(
            videoId = "sm12345",
            title = "テスト動画",
            thumbnailUrl = "test_url",
            createdAt = 1000L
        )
        val uiState = FavoriteVideoUiState(
            isLoading = false,
            favoriteVideos = listOf(favoriteVideo)
        )

        // 検証
        assertFalse(uiState.isFavoriteEmpty)
        assertFalse(uiState.isLoading)
        assertTrue(uiState.favoriteVideos.isNotEmpty())
    }

    @Test
    fun エラーメッセージがある場合isErrorがtrueになること() = runWithDescription {
        // テストデータ
        val uiState = FavoriteVideoUiState(
            errorMessage = "エラーが発生しました"
        )

        // 検証
        assertTrue(uiState.isError)
        assertFalse(uiState.isSuccess)
    }

    @Test
    fun エラーメッセージがない場合isErrorがfalseになること() = runWithDescription {
        // テストデータ
        val uiState = FavoriteVideoUiState(
            errorMessage = null
        )

        // 検証
        assertFalse(uiState.isError)
        assertTrue(uiState.isSuccess)
    }

    @Test
    fun ローディング中でエラーがない場合isSuccessがfalseになること() = runWithDescription {
        // テストデータ
        val uiState = FavoriteVideoUiState(
            isLoading = true,
            errorMessage = null
        )

        // 検証
        assertFalse(uiState.isSuccess)
        assertFalse(uiState.isError)
        assertTrue(uiState.isLoading)
    }

    @Test
    fun ローディングが完了しエラーがない場合isSuccessがtrueになること() = runWithDescription {
        // テストデータ
        val uiState = FavoriteVideoUiState(
            isLoading = false,
            errorMessage = null
        )

        // 検証
        assertTrue(uiState.isSuccess)
        assertFalse(uiState.isError)
        assertFalse(uiState.isLoading)
    }

    @Test
    fun ofDefaultでデフォルト値が正しく設定されること() = runWithDescription {
        // テスト実行
        val uiState = FavoriteVideoUiState.ofDefault()

        // 検証
        assertFalse(uiState.isLoading)
        assertTrue(uiState.favoriteVideos.isNotEmpty())
        assertTrue(uiState.favoriteVideos.size == 1)
        assertFalse(uiState.isError)
        assertTrue(uiState.isSuccess)
        assertFalse(uiState.isFavoriteEmpty)
    }

    @Test
    fun エラー状態とローディング状態の組み合わせテスト() = runWithDescription {
        // テストデータ：ローディング中でエラーがある場合
        val uiState = FavoriteVideoUiState(
            isLoading = true,
            errorMessage = "エラーが発生しました"
        )

        // 検証
        assertTrue(uiState.isError)
        assertFalse(uiState.isSuccess)
        assertTrue(uiState.isLoading)
    }

    @Test
    fun お気に入りが空でエラーがある場合のテスト() = runWithDescription {
        // テストデータ
        val uiState = FavoriteVideoUiState(
            isLoading = false,
            favoriteVideos = emptyList(),
            errorMessage = "エラーが発生しました"
        )

        // 検証
        assertTrue(uiState.isError)
        assertFalse(uiState.isSuccess)
        assertFalse(uiState.isLoading)
    }
}