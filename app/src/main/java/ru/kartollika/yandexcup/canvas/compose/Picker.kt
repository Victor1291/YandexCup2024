package ru.kartollika.yandexcup.canvas.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ru.kartollika.yandexcup.core.noIndicationClickable

@NonRestartableComposable
@Composable
fun Picker(
  modifier: Modifier = Modifier,
  content: @Composable BoxScope.() -> Unit = {},
) {
  Box(
    modifier = modifier
      .background(Color.Gray, RoundedCornerShape(4.dp))
      .padding(16.dp)
      .noIndicationClickable { },
    content = content
  )
}