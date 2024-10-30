package ru.kartollika.yandexcup.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
  primary = DarkAccent,
  surface = DarkSurface,
  onSurface = DarkOnSurface
)

private val LightColorScheme = lightColorScheme(
  primary = LightAccent,
  surface = LightSurface,
  onSurface = LightOnSurface
)

@Composable
fun YandexCup2024Theme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  content: @Composable () -> Unit,
) {
  val colorScheme = when {
    darkTheme -> DarkColorScheme
    else -> LightColorScheme
  }

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}