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
import com.neesan.core.valueClass.PitchKey

@Composable
fun PitchControllerComponent(currentKey: PitchKey, onPitchUp: () -> Unit, onPitchDown: () -> Unit) {
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
            text = "Key: ${currentKey.toKeyText()}",
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

@Preview(showBackground = true)
@Composable
fun PreviewPitchControllerComponent_標準のKey() {
    PitchControllerComponent(
        currentKey = PitchKey(0.0),
        onPitchUp = {},
        onPitchDown = {}
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewPitchControllerComponent_キー3つ上げ() {
    PitchControllerComponent(
        currentKey = PitchKey(3.0),
        onPitchUp = {},
        onPitchDown = {}
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewPitchControllerComponent_キー3つ下げ() {
    PitchControllerComponent(
        currentKey = PitchKey(-3.0),
        onPitchUp = {},
        onPitchDown = {}
    )
}
