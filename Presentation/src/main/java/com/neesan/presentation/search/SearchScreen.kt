package com.neesan.presentation.search

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.neesan.presentation.search.section.SearchResultsSection

/**
 * 検索画面
 */
@Composable
fun SearchScreen(
    viewModel: SearchVideoViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    SearchContent(
        uiState = uiState,
        onSearch = viewModel::search,
        onClearSearch = viewModel::clearSearch,
        onNotificationRequestFinish = {
            viewModel.updateNotificationPermissionRequested()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchContent(
    uiState: SearchVideoUiState,
    onSearch: (String) -> Unit,
    onClearSearch: () -> Unit,
    onNotificationRequestFinish: () -> Unit = {},
) {
    var searchQuery by remember { mutableStateOf("") }

    val requestPermissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
            // 権限ダイアログが閉じられた時
            onNotificationRequestFinish()
        }

    LaunchedEffect(Unit) {
        if (uiState.showNotificationPermissionDialog
            && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
        ) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("動画検索") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // 検索フィールド
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("検索キーワード") },
                leadingIcon = {
                    IconButton(onClick = { onSearch(searchQuery) }) {
                        Icon(Icons.Default.Search, contentDescription = "検索")
                    }
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = {
                            searchQuery = ""
                            onClearSearch()
                        }) {
                            Icon(Icons.Default.Clear, contentDescription = "クリア")
                        }
                    }
                },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ローディングインジケーター
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            // エラーメッセージ
            if (uiState.isError) {
                Text(
                    text = uiState.errorMessage ?: "エラーが発生しました",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            SearchResultsSection(uiState)
        }
    }
}

/**
 * Preview関数
 */
@Preview
@Composable
private fun PreviewSearchContent() {
    val previewState = SearchVideoUiState.ofDefault()

    SearchContent(
        uiState = previewState,
        onSearch = {},
        onClearSearch = {}
    )
}
