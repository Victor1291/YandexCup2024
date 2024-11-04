package ru.kartollika.yandexcup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import ru.kartollika.yandexcup.canvas.compose.CanvasScreen
import ru.kartollika.yandexcup.ui.theme.RippleTheme
import ru.kartollika.yandexcup.ui.theme.YandexCup2024Theme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    setContent {
      YandexCup2024Theme {
        CompositionLocalProvider(
          LocalRippleTheme provides RippleTheme(
            rippleColor = MaterialTheme.colorScheme.onSurface
          )
        ) {
          CanvasScreen(
            modifier = Modifier.fillMaxSize(),
          )
        }
      }
    }
  }

}