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
import com.neesan.presentation.favorite.FavoriteDestination
import com.neesan.presentation.favorite.FavoriteScreen
import com.neesan.presentation.search.SearchDestination
import com.neesan.presentation.search.SearchScreen
import com.neesan.presentation.videoplayer.VideoPlayerDestination
import com.neesan.presentation.videoplayer.VideoPlayerScreen
import com.neesan.domain.search.VideoDomainModel

/**
 * メイン画面。下部のタブで検索画面とお気に入り画面を切り替える。
 */
@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                NavigationBarItem(
                    selected = currentDestination?.hasRoute<SearchDestination>() == true,
                    onClick = {
                        navController.navigate(SearchDestination) {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = { androidx.compose.material3.Icon(SearchDestination.icon, contentDescription = SearchDestination.label) },
                    label = { Text(SearchDestination.label) }
                )

                NavigationBarItem(
                    selected = currentDestination?.hasRoute<FavoriteDestination>() == true,
                    onClick = {
                        navController.navigate(FavoriteDestination) {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = { androidx.compose.material3.Icon(FavoriteDestination.icon, contentDescription = FavoriteDestination.label) },
                    label = { Text(FavoriteDestination.label) }
                )
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
                        navController.currentBackStackEntry?.savedStateHandle?.set("video", video)
                        navController.navigate(VideoPlayerDestination)
                    }
                )
            }
            composable<FavoriteDestination> {
                FavoriteScreen(
                    onVideoClick = { video ->
                        navController.currentBackStackEntry?.savedStateHandle?.set("video", video)
                        navController.navigate(VideoPlayerDestination)
                    }
                )
            }
            composable<VideoPlayerDestination> { backStackEntry ->
                val video = backStackEntry.savedStateHandle.get<VideoDomainModel>("video")
                VideoPlayerScreen(video)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewMainScreen() {
    MainScreen()
}

// navItems no longer needed thanks to explicit items for type safety 