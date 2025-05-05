package com.neesan.nicokeyshifter2.search.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neesan.nicokeyshifter2.search.domain.SearchVideoUseCase
import com.neesan.nicokeyshifter2.search.domain.exception.SearchException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 検索画面のViewModel
 */
@HiltViewModel
class SearchVideoViewModel @Inject constructor(
    private val searchVideoUseCase: SearchVideoUseCase
) : ViewModel() {

    // UI状態
    private val _uiState = MutableStateFlow(SearchVideoUiState())
    val uiState: StateFlow<SearchVideoUiState> = _uiState

    /**
     * 検索を実行する
     *
     * @param query 検索キーワード
     * @param targets 検索対象（デフォルトはタイトル）
     * @param sort ソート方法（デフォルトは再生数降順）
     * @param limit 取得件数（デフォルトは100件）
     */
    fun search(
        query: String,
        targets: String = "title",
        sort: String = "-viewCounter",
        limit: Int = 100
    ) {
        // 検索キーワードが空の場合は検索しない
        if (query.isBlank()) {
            _uiState.value = SearchVideoUiState()
            return
        }

        viewModelScope.launch {
            searchVideoUseCase.invoke(query, targets, sort, limit)
                .onStart {
                    // ローディング状態に更新
                    _uiState.update { currentState ->
                        currentState.copy(
                            isLoading = true,
                            query = query,
                            errorMessage = null
                        )
                    }
                }
                .onEach { videos ->
                    // 検索結果を更新
                    _uiState.update { currentState ->
                        currentState.copy(
                            isLoading = false,
                            videos = videos
                        )
                    }
                }
                .catch { throwable ->
                    throwable.printStackTrace()
                    // エラーメッセージを設定
                    val errorMessage = when (throwable) {
                        is SearchException.ApiError -> "APIエラーが発生しました (${throwable.statusCode})"
                        else -> "予期せぬエラーが発生しました"
                    }
                    _uiState.update { currentState ->
                        currentState.copy(
                            isLoading = false,
                            errorMessage = errorMessage
                        )
                    }
                }
                .launchIn(viewModelScope)
        }
    }

    /**
     * 検索をクリアする
     */
    fun clearSearch() {
        _uiState.value = SearchVideoUiState()
    }
} 