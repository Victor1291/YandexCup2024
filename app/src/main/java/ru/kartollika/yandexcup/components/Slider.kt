package ru.kartollika.yandexcup.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SliderState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale.Companion
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.kartollika.yandexcup.R
import ru.kartollika.yandexcup.ui.theme.YandexCup2024Theme

@OptIn(ExperimentalMaterial3Api::class)
@NonRestartableComposable
@Composable
fun Slider(
  value: Float,
  onValueChange: (Float) -> Unit,
  modifier: Modifier = Modifier,
  valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
  thumb: @Composable (SliderState) -> Unit = {
    Spacer(
      modifier = Modifier
        .size(24.dp)
        .background(Color.White, CircleShape)
    )
  },
  invertTrack: Boolean = false,
) {
  androidx.compose.material3.Slider(
    value = value,
    onValueChange = onValueChange,
    modifier = modifier,
    valueRange = valueRange,
    thumb = thumb,
    track = {
      Image(
        modifier = Modifier
          .fillMaxWidth()
          .rotate(if (invertTrack) 180f else 0f),
        contentScale = Companion.FillBounds,
        painter = painterResource(R.drawable.track),
        contentDescription = null
      )
    },
  )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun SliderPreview() {
  var progress by remember { mutableFloatStateOf(0f) }
  YandexCup2024Theme {
    Slider(
      value = progress,
      onValueChange = { progress = it },
      thumb = { sliderState ->
        Spacer(
          modifier = Modifier
            .size(20.dp)
            .background(Color.White, CircleShape)
        )
      }
    )
  }
}