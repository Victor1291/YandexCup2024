package ru.kartollika.yandexcup.canvas.compose.picker

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.kartollika.yandexcup.canvas.mvi.EditorConfiguration
import ru.kartollika.yandexcup.components.Slider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrushSizePicker(
  editorConfiguration: EditorConfiguration,
  modifier: Modifier = Modifier,
  changeBrushSize: (Float) -> Unit = {},
) {
  Box(
    modifier = modifier,
  ) {
    Slider(
      modifier = Modifier
        .fillMaxSize(),
      value = editorConfiguration.brushSizeByMode,
      valueRange = 4f..100f,
      onValueChange = { value ->
        changeBrushSize(value)
      },
      invertTrack = true
    )
  }
}