package com.neesan.presentation.search.section

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
import com.neesan.presentation.search.SearchVideoUiState
import com.neesan.presentation.search.component.VideoItemRowComponent

/**
 * 検索画面
 */
@Composable
fun SearchResultsSection(uiState: SearchVideoUiState) {
    // 検索結果
    when {
        // 初期状態（検索前）
        uiState.isInitialState -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 32.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                Text("キーワードを入力して検索してください")
            }
        }
        // 検索結果が空
        uiState.isSearchResultEmpty -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 32.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                Text("「${uiState.query}」の検索結果が見つかりませんでした")
            }
        }

        // 検索結果あり
        uiState.isSuccess && uiState.videos.isNotEmpty() -> {
            Column {
                Text(
                    "「${uiState.query}」の検索結果: ${uiState.videos.size}件",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.videos) { video ->
                        VideoItemRowComponent(video)
                    }
                }
            }
        }
    }
}

/**
 * Preview関数
 */
@Preview
@Composable
private fun PreviewSearchResultsSection() {
    val previewState = SearchVideoUiState.ofDefault()
    SearchResultsSection(
        uiState = previewState
    )
}
