package com.neesan.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.neesan.presentation.favorite.FavoriteDestination
import com.neesan.presentation.favorite.FavoriteScreen
import com.neesan.presentation.search.SearchDestination
import com.neesan.presentation.search.SearchScreen
import com.neesan.presentation.player.PlayerDestination
import com.neesan.presentation.player.VideoPlayerScreen

// ボトムナビゲーションを適用したいスクリーンのリスト
// navControllerのAPIの都合でjavaClass.nameを使う
val topRoutes = listOf(SearchDestination.javaClass.name, FavoriteDestination.javaClass.name)

/**
 * メイン画面。下部のタブで検索画面とお気に入り画面を切り替える。
 */
@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination
            val currentRoute = currentDestination?.route ?: ""

            if (topRoutes.contains(currentRoute)) {
                NavigationBar {
                    NavigationBarItem(
                        selected = currentDestination?.hasRoute<SearchDestination>() == true,
                        onClick = {
                            navController.navigate(SearchDestination) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            androidx.compose.material3.Icon(
                                SearchDestination.icon,
                                contentDescription = SearchDestination.label
                            )
                        },
                        label = { Text(SearchDestination.label) }
                    )

                    NavigationBarItem(
                        selected = currentDestination?.hasRoute<FavoriteDestination>() == true,
                        onClick = {
                            navController.navigate(FavoriteDestination) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            androidx.compose.material3.Icon(
                                FavoriteDestination.icon,
                                contentDescription = FavoriteDestination.label
                            )
                        },
                        label = { Text(FavoriteDestination.label) }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = SearchDestination,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<SearchDestination> {
                SearchScreen(
                    onVideoClick = { video ->
                        navController.navigate(
                            PlayerDestination(
                                videoId = video.id,
                                title = video.title,
                                thumbnailUrl = video.thumbnailUrl,
                                isFavorite = video.isFavorite
                            )
                        )
                    }
                )
            }
            composable<FavoriteDestination> {
                FavoriteScreen(
                    onVideoClick = { video ->
                        navController.navigate(
                            PlayerDestination(
                                videoId = video.id,
                                title = video.title,
                                thumbnailUrl = video.thumbnailUrl,
                                isFavorite = video.isFavorite
                            )
                        )
                    }
                )
            }
            composable<PlayerDestination> { backStackEntry ->
                val destination = backStackEntry.toRoute<PlayerDestination>()
                VideoPlayerScreen(destination)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewMainScreen() {
    MainScreen()
}
