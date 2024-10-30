package ru.kartollika.yandexcup.canvas.compose

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.collections.immutable.ImmutableList
import ru.kartollika.yandexcup.canvas.CanvasDrawUiState
import ru.kartollika.yandexcup.canvas.mvi.PathWithProperties

@Composable
@NonRestartableComposable
fun DrawingCanvas(
  paths: () -> ImmutableList<PathWithProperties>,
//  currentPath: () -> PathWithProperties?,
  previousPaths: () -> ImmutableList<PathWithProperties>?,
  modifier: Modifier = Modifier,
  canvasDrawUiState: CanvasDrawUiState,
  onDragStart: (Offset) -> Unit = {},
  onDragEnd: () -> Unit = {},
  onDragCancel: () -> Unit = {},
  onDrag: (Offset) -> Unit = {},
  scale: Float = 1f
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
    scale(scale, scale, pivot = Offset(0f, 0f)) {
      with(drawContext.canvas.nativeCanvas) {
        val checkPoint = saveLayer(null, null)
        previousPaths()?.forEach { pathWithProperties ->
          drawPath(
            path = pathWithProperties.path,
            color = pathWithProperties.properties.color.copy(alpha = 0.3f),
            style = Stroke(
              width = pathWithProperties.properties.brushSize,
              cap = StrokeCap.Round,
              join = StrokeJoin.Round
            ),
            blendMode = resolveBlendMode(pathWithProperties.properties.eraseMode)
          )
        }
        restoreToCount(checkPoint)
      }
    }

    scale(scale, scale, pivot = Offset(0f, 0f)) {
      with(drawContext.canvas.nativeCanvas) {
        val checkPoint = saveLayer(null, null)
        paths().forEach { pathWithProperties ->
          drawPath(
            path = pathWithProperties.path,
            color = pathWithProperties.properties.color,
            style = Stroke(
              width = pathWithProperties.properties.brushSize,
              cap = StrokeCap.Round,
              join = StrokeJoin.Round
            ),
            blendMode = resolveBlendMode(pathWithProperties.properties.eraseMode)
          )
        }


        canvasDrawUiState.currentPath?.let { pathWithProperties ->
          drawPath(
            path = pathWithProperties.path,
            color = pathWithProperties.properties.color,
            style = Stroke(
              width = pathWithProperties.properties.brushSize,
              cap = StrokeCap.Round,
              join = StrokeJoin.Round
            ),
            blendMode = resolveBlendMode(pathWithProperties.properties.eraseMode)
          )
        }
        restoreToCount(checkPoint)
      }
    }
  }
}

private fun resolveBlendMode(isEraseMode: Boolean): BlendMode = if (isEraseMode) {
  BlendMode.Clear
} else {
  BlendMode.SrcOver
}