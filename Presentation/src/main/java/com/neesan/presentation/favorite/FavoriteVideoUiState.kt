package com.neesan.presentation.favorite

import com.neesan.domain.favorite.FavoriteVideoDomainData

/**
 * お気に入り画面のUI状態
 *
 * @property isLoading 読み込み中かどうか
 * @property favoriteVideos お気に入り動画リスト
 * @property errorMessage エラーメッセージ（エラーがない場合はnull）
 */
data class FavoriteVideoUiState(
    val isLoading: Boolean = false,
    val favoriteVideos: List<FavoriteVideoDomainData> = emptyList(),
    val errorMessage: String? = null
) {
    /**
     * お気に入りが空かどうか
     */
    val isFavoriteEmpty: Boolean
        get() = !isLoading && favoriteVideos.isEmpty()

    /**
     * エラー状態かどうか
     */
    val isError: Boolean
        get() = errorMessage != null

    /**
     * 成功状態かどうか
     */
    val isSuccess: Boolean
        get() = !isLoading && errorMessage == null

    companion object {
        fun ofDefault() = FavoriteVideoUiState(
            isLoading = false,
            favoriteVideos = listOf(
                FavoriteVideoDomainData.ofDefault()
            ),
            errorMessage = null
        )
    }
}