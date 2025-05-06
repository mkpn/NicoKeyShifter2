package com.neesan.presentation.search

import com.neesan.domain.search.VideoDomainModel

/**
 * 検索画面のUI状態
 *
 * @property isLoading 読み込み中かどうか
 * @property query 検索クエリ
 * @property videos 検索結果の動画リスト
 * @property errorMessage エラーメッセージ（エラーがない場合はnull）
 * @property isEmpty 検索結果が空かどうか (検索実行後に結果が0件の場合はtrue)
 * @property isInitialState 初期状態かどうか（検索実行前）
 */
data class SearchVideoUiState(
    val isLoading: Boolean = false,
    val query: String = "",
    val videos: List<VideoDomainModel> = emptyList(),
    val errorMessage: String? = null,
) {
    /**
     * 検索結果が空かどうか
     * 検索実行後に結果が0件の場合はtrue
     */
    val isEmpty: Boolean
        get() = !isLoading && query.isNotEmpty() && videos.isEmpty() && errorMessage == null

    /**
     * 初期状態かどうか（検索実行前）
     */
    val isInitialState: Boolean
        get() = !isLoading && query.isEmpty() && videos.isEmpty() && errorMessage == null

    /**
     * エラー状態かどうか
     */
    val isError: Boolean
        get() = errorMessage != null

    /**
     * 検索成功状態かどうか (結果が0件の場合もtrueになる)
     */
    val isSuccess: Boolean
        get() = !isLoading && query.isNotEmpty() && errorMessage == null

    companion object {
        fun ofDefault() = SearchVideoUiState(
            isLoading = false,
            videos = listOf(
                VideoDomainModel(
                    id = "sm12345678",
                    title = "サンプル動画タイトル",
                    thumbnailUrl = "",
                    viewCount = 12345
                )
            ),
            errorMessage = null
        )
    }
}
