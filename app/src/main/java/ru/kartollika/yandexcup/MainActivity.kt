package ru.kartollika.yandexcup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import ru.kartollika.yandexcup.canvas.CanvasScreen
import ru.kartollika.yandexcup.ui.theme.YandexCup2024Theme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    setContent {
      YandexCup2024Theme {
        CanvasScreen(
          modifier = Modifier.fillMaxSize(),
        )
      }
    }
  }

}