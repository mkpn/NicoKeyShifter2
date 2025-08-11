package com.neesan.presentation.favorite.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.neesan.domain.favorite.FavoriteVideoDomainData
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.absoluteValue

@Composable
fun FavoriteVideoItemComponent(
    favoriteVideo: FavoriteVideoDomainData,
    onRemoveFavorite: (String) -> Unit,
    onVideoClick: (FavoriteVideoDomainData) -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onVideoClick(favoriteVideo) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current)
                        .data(favoriteVideo.thumbnailUrl)
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
                Text(
                    text = favoriteVideo.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "登録日: ${formatDate(favoriteVideo.createdAt)}",
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = "ID: ${favoriteVideo.videoId}",
                    style = MaterialTheme.typography.bodySmall
                )
                
                Text(
                    text = "キー: ${generateKeyText(favoriteVideo.keyValue)}",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            IconButton(
                onClick = { onRemoveFavorite(favoriteVideo.videoId) }
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "お気に入りから削除",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

private fun formatDate(timestamp: Long): String {
    val date = Date(timestamp)
    val formatter = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
    return formatter.format(date)
}

private fun generateKeyText(key: Double): String {
    val prefix = if (key == 0.0) "±" else if (key > 0) "♯" else "♭"

    val absoluteKey = key.absoluteValue.toInt()
    return "$prefix$absoluteKey"
}

@Preview
@Composable
private fun PreviewFavoriteVideoItemComponent() {
    FavoriteVideoItemComponent(
        favoriteVideo = FavoriteVideoDomainData.ofDefault(),
        onRemoveFavorite = {},
        onVideoClick = {}
    )
}
