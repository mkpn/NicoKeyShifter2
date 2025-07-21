package com.neesan.presentation.videoplayer

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.ui.graphics.vector.ImageVector
import com.neesan.presentation.NavigationDestination
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

/**
 * 動画再生画面の遷移先情報
 */
@Serializable
object VideoPlayerDestination : NavigationDestination {
    override val route: String = "video_player"
    override val label: String = "動画再生"

    @Transient
    override val icon: ImageVector = Icons.Filled.PlayArrow
} 