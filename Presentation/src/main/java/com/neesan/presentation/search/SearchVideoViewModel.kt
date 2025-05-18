package com.neesan.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neesan.core.exception.SearchException
import com.neesan.domain.notification.CheckNotificationPermissionRequestedUseCase
import com.neesan.domain.notification.UpdateNotificationPermissionRequestedUseCase
import com.neesan.domain.search.SearchVideoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

/**
 * 検索画面のViewModel
 */
@HiltViewModel
class SearchVideoViewModel @Inject constructor(
    private val searchVideoUseCase: SearchVideoUseCase,
    private val checkNotificationPermissionRequestedUseCase: CheckNotificationPermissionRequestedUseCase,
    private val updateNotificationPermissionRequestedUseCase: UpdateNotificationPermissionRequestedUseCase
) : ViewModel() {

    // UI状態
    private val _uiState = MutableStateFlow(SearchVideoUiState())
    val uiState: StateFlow<SearchVideoUiState> = _uiState

    init {
        runBlocking {
            // 通知の権限がリクエストされたかどうかを確認
            val isNotificationPermissionRequested = checkNotificationPermissionRequestedUseCase.invoke()

            // 過去に通知の権限がリクエストされていない場合、ダイアログを表示する
            if(!isNotificationPermissionRequested) {
                _uiState.update {
                    it.copy(
                        showNotificationPermissionDialog = true
                    )
                }
            }
        }
    }

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

    /**
     * 通知の権限をリクエスト済みの状態に更新する
     * uiStateのダイアログ表示フラグもfalseにする
     */
    fun updateNotificationPermissionRequested() {
        viewModelScope.launch {
            updateNotificationPermissionRequestedUseCase.invoke()
            _uiState.update { currentState ->
                currentState.copy(
                    showNotificationPermissionDialog = false
                )
            }
        }
    }
}
