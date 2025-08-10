@file:Suppress("NonAsciiCharacters")

package com.neesan.presentation.player.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement.SpaceEvenly
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import kotlin.math.absoluteValue

@Composable
fun PitchControllerComponent(currentKey: Double, onPitchUp: () -> Unit, onPitchDown: () -> Unit) {
    // ボタンを等間隔に並べるRow
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // ピッチ調整ボタン
        Text(
            modifier = Modifier
                .weight(1f)
                .clickable{
                    onPitchDown()
                },
            text = "♭",
            textAlign = TextAlign.Center
        )
        Text(
            text = "Key: ${generateKeyText(currentKey)}",
            textAlign = TextAlign.Center
        )
        Text(
            modifier = Modifier
                .weight(1f)
                .clickable {
                    onPitchUp()
                },
            text = "♯",
            textAlign = TextAlign.Center
        )
    }
}

private fun generateKeyText(key: Double): String {
    // 絶対値に♯と♭をつけたテキストに変換する
    // 原曲キーの場合は±をつける
    val prefix = if (key == 0.0) "±" else if (key > 0) "♯" else "♭"

    val absoluteKey = key.absoluteValue.toInt()
    return "$prefix$absoluteKey"
}

@Preview(showBackground = true)
@Composable
fun PreviewPitchControllerComponent_標準のKey() {
    PitchControllerComponent(
        currentKey = 0.0,
        onPitchUp = {},
        onPitchDown = {}
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewPitchControllerComponent_キー3つ上げ() {
    PitchControllerComponent(
        currentKey = 3.0,
        onPitchUp = {},
        onPitchDown = {}
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewPitchControllerComponent_キー3つ下げ() {
    PitchControllerComponent(
        currentKey = -3.0,
        onPitchUp = {},
        onPitchDown = {}
    )
}
