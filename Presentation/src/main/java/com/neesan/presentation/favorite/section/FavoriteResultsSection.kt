package com.neesan.presentation.favorite.section

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.neesan.presentation.favorite.FavoriteVideoUiState
import com.neesan.presentation.favorite.component.FavoriteVideoItemComponent

@Composable
fun FavoriteResultsSection(
    uiState: FavoriteVideoUiState,
    onRemoveFavorite: (String) -> Unit
) {
    when {
        uiState.isFavoriteEmpty && !uiState.isLoading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 32.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                Text("お気に入りに登録された動画がありません")
            }
        }

        uiState.isSuccess && uiState.favoriteVideos.isNotEmpty() -> {
            Column {
                Text(
                    "お気に入り動画: ${uiState.favoriteVideos.size}件",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.favoriteVideos) { favoriteVideo ->
                        FavoriteVideoItemComponent(
                            favoriteVideo = favoriteVideo,
                            onRemoveFavorite = onRemoveFavorite
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewFavoriteResultsSection() {
    val previewState = FavoriteVideoUiState.ofDefault()
    FavoriteResultsSection(
        uiState = previewState,
        onRemoveFavorite = {}
    )
}
