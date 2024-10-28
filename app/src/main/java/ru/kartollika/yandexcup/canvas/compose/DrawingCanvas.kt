package ru.kartollika.yandexcup.canvas.compose

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import ru.kartollika.yandexcup.canvas.mvi.PathWithProperties

@Composable
fun DrawingCanvas(
  paths: () -> List<PathWithProperties>,
  currentPath: () -> PathWithProperties?,
  modifier: Modifier = Modifier,
  onDragStart: (Offset) -> Unit = {},
  onDragEnd: () -> Unit = {},
  onDragCancel: () -> Unit = {},
  onDrag: (Offset) -> Unit = {},
) {
  val drawModifier = Modifier
    .pointerInput(Unit) {
      detectDragGestures(
        onDragStart = { onDragStart(it) },
        onDragEnd = { onDragEnd() },
        onDragCancel = { onDragCancel() },
        onDrag = { _, amount -> onDrag(amount) }
      )
    }

  Canvas(
    modifier = modifier
      .then(drawModifier),
  ) {
    paths().forEach { pathWithProperties ->
      drawPath(
        path = pathWithProperties.path,
        color = pathWithProperties.properties.color,
        style = Stroke(
          width = 4.dp.toPx(),
          cap = StrokeCap.Round,
          join = StrokeJoin.Round
        )
      )
    }

    currentPath()?.let { pathWithProperties ->
      drawPath(
        path = pathWithProperties.path,
        color = pathWithProperties.properties.color,
        style = Stroke(
          width = 4.dp.toPx(),
          cap = StrokeCap.Round,
          join = StrokeJoin.Round
        ),
      )
    }
  }
}