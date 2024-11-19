package ru.kartollika.yandexcup.canvas.compose

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTransformGestures
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
import ru.kartollika.yandexcup.canvas.CanvasMode.Transform
import ru.kartollika.yandexcup.canvas.mvi.PathWithProperties

fun interface OnTransform {
  operator fun invoke(
    centroid: Offset,
    pan: Offset,
    zoom: Float,
    rotation: Float,
  )
}

@Composable

@NonRestartableComposable
fun DrawingCanvas(
  paths: () -> ImmutableList<PathWithProperties>?,
  previousPaths: () -> ImmutableList<PathWithProperties>?,
  modifier: Modifier = Modifier,
  canvasDrawUiState: CanvasDrawUiState,
  onDragStart: (Offset) -> Unit = {},
  onDragEnd: () -> Unit = {},
  onDragCancel: () -> Unit = {},
  onDrag: (Offset) -> Unit = {},
  scale: Float = 1f,
  onTransform: OnTransform = OnTransform { centroid, pan, zoom, rotation -> },
  onCanvasTransform: OnTransform = OnTransform { centroid, pan, zoom, rotation -> },
  backgroundModifier: Modifier = Modifier,
) {
  val drawModifier = Modifier
    .pointerInput(canvasDrawUiState.mode) {
      if (canvasDrawUiState.mode == Transform) {
        detectTransformGestures(true) { centroid, pan, zoom, rotation ->
          onTransform(centroid, pan, zoom, rotation)
          println("$centroid $pan $zoom $rotation")
        }
      }
    }
    .pointerInput(canvasDrawUiState.mode) {
      if (canvasDrawUiState.mode == Transform) return@pointerInput
      detectPointerTransformGestures(
        onDragStart = {
          onDragStart(it)
        },
        onDrag = { offset ->
          onDrag(offset)
        },
        onDragCancelled = {
          onDragCancel()
        },
        onTransform = {
            gestureCentroid: Offset,
            gesturePan: Offset,
            gestureZoom: Float,
            gestureRotation,
          ->
          onCanvasTransform(gestureCentroid, gesturePan, gestureZoom, gestureRotation)
        },
        onDragEnd = {
          onDragEnd()
        }
      )
    }

  Canvas(
    modifier = modifier
      .then(drawModifier)
      .then(backgroundModifier),
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
        paths()?.forEach { pathWithProperties ->
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