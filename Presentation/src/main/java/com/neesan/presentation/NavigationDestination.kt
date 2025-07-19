package com.neesan.presentation

import androidx.compose.ui.graphics.vector.ImageVector

/**
 * ボトムナビゲーションなどで使用する画面遷移先を表す共通インターフェース。
 */
interface NavigationDestination {
    val route: String
    val label: String
    val icon: ImageVector
} 