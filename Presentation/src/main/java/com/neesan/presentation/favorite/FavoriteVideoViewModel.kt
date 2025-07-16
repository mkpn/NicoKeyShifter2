package com.neesan.presentation.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neesan.domain.favorite.AddFavoriteVideoUseCase
import com.neesan.domain.favorite.CheckIsFavoriteUseCase
import com.neesan.domain.favorite.FavoriteVideoDomainData
import com.neesan.domain.favorite.GetAllFavoriteVideosUseCase
import com.neesan.domain.favorite.GetFavoriteVideoByIdUseCase
import com.neesan.domain.favorite.RemoveFavoriteVideoByIdUseCase
import com.neesan.domain.favorite.RemoveFavoriteVideoUseCase
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
 * お気に入り画面のViewModel
 */
@HiltViewModel
class FavoriteVideoViewModel @Inject constructor(
    private val getAllFavoriteVideosUseCase: GetAllFavoriteVideosUseCase,
    private val getFavoriteVideoByIdUseCase: GetFavoriteVideoByIdUseCase,
    private val addFavoriteVideoUseCase: AddFavoriteVideoUseCase,
    private val removeFavoriteVideoUseCase: RemoveFavoriteVideoUseCase,
    private val removeFavoriteVideoByIdUseCase: RemoveFavoriteVideoByIdUseCase,
    private val checkIsFavoriteUseCase: CheckIsFavoriteUseCase
) : ViewModel() {

    // UI状態
    private val _uiState = MutableStateFlow(FavoriteVideoUiState())
    val uiState: StateFlow<FavoriteVideoUiState> = _uiState

    init {
        loadFavoriteVideos()
    }

    /**
     * お気に入り動画一覧を読み込む
     */
    fun loadFavoriteVideos() {
        viewModelScope.launch {
            getAllFavoriteVideosUseCase.invoke()
                .onStart {
                    // ローディング状態に更新
                    _uiState.update { currentState ->
                        currentState.copy(
                            isLoading = true,
                            errorMessage = null
                        )
                    }
                }
                .onEach { favoriteVideos ->
                    // お気に入り動画一覧を更新
                    _uiState.update { currentState ->
                        currentState.copy(
                            isLoading = false,
                            favoriteVideos = favoriteVideos
                        )
                    }
                }
                .catch { throwable ->
                    throwable.printStackTrace()
                    // エラーメッセージを設定
                    val errorMessage = "お気に入り動画の読み込みに失敗しました"
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
     * お気に入りに動画を追加する
     *
     * @param favoriteVideo 追加するお気に入り動画データ
     */
    fun addFavoriteVideo(favoriteVideo: FavoriteVideoDomainData) {
        viewModelScope.launch {
            try {
                addFavoriteVideoUseCase.invoke(favoriteVideo)
                // お気に入り一覧を再読み込み
                loadFavoriteVideos()
            } catch (throwable: Throwable) {
                throwable.printStackTrace()
                _uiState.update { currentState ->
                    currentState.copy(
                        errorMessage = "お気に入りの追加に失敗しました"
                    )
                }
            }
        }
    }

    /**
     * お気に入りから動画を削除する
     *
     * @param favoriteVideo 削除するお気に入り動画データ
     */
    fun removeFavoriteVideo(favoriteVideo: FavoriteVideoDomainData) {
        viewModelScope.launch {
            try {
                removeFavoriteVideoUseCase.invoke(favoriteVideo)
                // お気に入り一覧を再読み込み
                loadFavoriteVideos()
            } catch (throwable: Throwable) {
                throwable.printStackTrace()
                _uiState.update { currentState ->
                    currentState.copy(
                        errorMessage = "お気に入りの削除に失敗しました"
                    )
                }
            }
        }
    }

    /**
     * 動画IDでお気に入りから削除する
     *
     * @param videoId 削除する動画ID
     */
    fun removeFavoriteVideoById(videoId: String) {
        viewModelScope.launch {
            try {
                removeFavoriteVideoByIdUseCase.invoke(videoId)
                // お気に入り一覧を再読み込み
                loadFavoriteVideos()
            } catch (throwable: Throwable) {
                throwable.printStackTrace()
                _uiState.update { currentState ->
                    currentState.copy(
                        errorMessage = "お気に入りの削除に失敗しました"
                    )
                }
            }
        }
    }

    /**
     * エラーメッセージをクリアする
     */
    fun clearError() {
        _uiState.update { currentState ->
            currentState.copy(errorMessage = null)
        }
    }
}