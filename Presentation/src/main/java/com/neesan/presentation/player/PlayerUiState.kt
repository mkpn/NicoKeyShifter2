package com.neesan.presentation.player

import com.neesan.core.valueClass.PitchKey

data class PlayerUiState(
    val videoId: String = "",
    val title: String = "",
    val thumbnailUrl: String = "",
    val currentKey: PitchKey = PitchKey(0.0),
    val isFavorite: Boolean = false,
    val isLoading: Boolean = false,
)
