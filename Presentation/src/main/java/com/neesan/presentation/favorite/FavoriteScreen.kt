package com.neesan.presentation.favorite

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.neesan.presentation.favorite.section.FavoriteResultsSection

@Composable
fun FavoriteScreen(
    viewModel: FavoriteVideoViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    FavoriteContent(
        uiState = uiState,
        onRemoveFavorite = viewModel::removeFavoriteVideoById,
        onClearError = viewModel::clearError,
        onRetry = viewModel::loadFavoriteVideos
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FavoriteContent(
    uiState: FavoriteVideoUiState,
    onRemoveFavorite: (String) -> Unit,
    onClearError: () -> Unit,
    onRetry: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("お気に入り") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            if (uiState.isError) {
                Text(
                    text = uiState.errorMessage ?: "エラーが発生しました",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            FavoriteResultsSection(
                uiState = uiState,
                onRemoveFavorite = onRemoveFavorite
            )
        }
    }
}

@Preview
@Composable
private fun PreviewFavoriteContent() {
    val previewState = FavoriteVideoUiState.ofDefault()

    FavoriteContent(
        uiState = previewState,
        onRemoveFavorite = {},
        onClearError = {},
        onRetry = {}
    )
}
