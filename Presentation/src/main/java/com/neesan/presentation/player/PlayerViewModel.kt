package com.neesan.presentation.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neesan.core.valueClass.PitchKey
import com.neesan.domain.favorite.AddFavoriteVideoUseCase
import com.neesan.domain.favorite.CheckIsFavoriteUseCase
import com.neesan.domain.favorite.FavoriteVideoDomainData
import com.neesan.domain.favorite.GetFavoriteVideoByIdUseCase
import com.neesan.domain.favorite.RemoveFavoriteVideoByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 再生画面のViewModel
 */
@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val addFavoriteVideoUseCase: AddFavoriteVideoUseCase,
    private val removeFavoriteVideoByIdUseCase: RemoveFavoriteVideoByIdUseCase,
    private val checkIsFavoriteUseCase: CheckIsFavoriteUseCase,
    private val getFavoriteVideoByIdUseCase: GetFavoriteVideoByIdUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(PlayerUiState())
    val uiState: StateFlow<PlayerUiState> = _uiState

    private var initialized = false

    /**
     * 遷移パラメータで画面を初期化する。
     * すでにお気に入り登録済みの場合は保存済みのキー値を復元する。
     */
    fun initialize(destination: PlayerDestination) {
        if (initialized) return
        initialized = true

        _uiState.update {
            it.copy(
                videoId = destination.videoId,
                title = destination.title,
                thumbnailUrl = destination.thumbnailUrl,
                isFavorite = destination.isFavorite,
            )
        }

        viewModelScope.launch {
            val isFavorite = checkIsFavoriteUseCase.invoke(destination.videoId).first()
            val savedKey = if (isFavorite) {
                getFavoriteVideoByIdUseCase.invoke(destination.videoId).first()?.keyValue
                    ?: PitchKey(0.0)
            } else {
                PitchKey(0.0)
            }
            _uiState.update {
                it.copy(
                    isFavorite = isFavorite,
                    currentKey = savedKey,
                )
            }
        }
    }

    fun onPitchUp() {
        _uiState.update { it.copy(currentKey = it.currentKey + 1.0) }
    }

    fun onPitchDown() {
        _uiState.update { it.copy(currentKey = it.currentKey - 1.0) }
    }

    /**
     * お気に入り状態を切り替える。
     * 追加時は現在のキーをそのまま保存する。
     */
    fun toggleFavorite() {
        val current = _uiState.value
        if (current.videoId.isBlank()) return

        viewModelScope.launch {
            if (current.isFavorite) {
                removeFavoriteVideoByIdUseCase.invoke(current.videoId)
                _uiState.update { it.copy(isFavorite = false) }
            } else {
                addFavoriteVideoUseCase.invoke(
                    FavoriteVideoDomainData(
                        videoId = current.videoId,
                        title = current.title,
                        thumbnailUrl = current.thumbnailUrl,
                        createdAt = System.currentTimeMillis(),
                        keyValue = current.currentKey,
                    )
                )
                _uiState.update { it.copy(isFavorite = true) }
            }
        }
    }
}
