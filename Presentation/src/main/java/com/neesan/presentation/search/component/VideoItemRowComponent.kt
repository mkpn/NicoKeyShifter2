package com.neesan.presentation.search.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.neesan.domain.search.VideoDomainModel

/**
 * 動画アイテム
 */
@Composable
fun VideoItemRowComponent(video: VideoDomainModel) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // サムネイル
            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current)
                        .data(video.thumbnailUrl)
                        .crossfade(true)
                        .build()
                ),
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            ) {
                // タイトル
                Text(
                    text = video.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                // 再生回数
                Text(
                    text = "再生数: ${formatViewCount(video.viewCount)}",
                    style = MaterialTheme.typography.bodyMedium
                )

                // ID
                Text(
                    text = "ID: ${video.id}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

/**
 * 再生回数をフォーマットする
 */
private fun formatViewCount(count: Int): String {
    return when {
        count >= 10000 -> "${count / 10000}万"
        count >= 1000 -> "${count / 1000}千"
        else -> count.toString()
    }
}


/**
 * Preview関数
 */
@Preview
@Composable
private fun PreviewVideoItemRowComponent() {
    VideoItemRowComponent(
        video = VideoDomainModel.ofDefault()
    )
}
