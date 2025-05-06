package com.neesan.nicokeyshifter2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.neesan.nicokeyshifter2.presentation.search.SearchScreen
import com.neesan.nicokeyshifter2.ui.theme.NicoKeyShifter2Theme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NicoKeyShifter2Theme {
                    SearchScreen()
            }
        }
    }
}