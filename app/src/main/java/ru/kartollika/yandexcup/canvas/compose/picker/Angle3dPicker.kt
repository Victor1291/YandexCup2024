package ru.kartollika.yandexcup.canvas.compose.picker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.kartollika.yandexcup.canvas.mvi.EditorConfiguration
import ru.kartollika.yandexcup.components.Slider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Angle3dPicker(
  editorConfiguration: EditorConfiguration,
  modifier: Modifier = Modifier,
  changeBrushSize: (Float) -> Unit = {},
  rotate: (Float) -> Unit = {},
) {
  Box(
    modifier = modifier,
  ) {

    Column(
      verticalArrangement = Arrangement.SpaceAround
    ) {
      Slider(
        modifier = Modifier
          .fillMaxWidth(),
        value = editorConfiguration.brushSizeByMode,
        valueRange = 4f..100f,
        onValueChange = { value ->
          changeBrushSize(value)
        },
        invertTrack = true
      )

      Slider(
        modifier = Modifier
          .fillMaxWidth(),
        value = editorConfiguration.brushSizeByMode,
        valueRange = 4f..100f,
        onValueChange = { value ->
          rotate(value)
        },
        invertTrack = true
      )
    }
  }
}