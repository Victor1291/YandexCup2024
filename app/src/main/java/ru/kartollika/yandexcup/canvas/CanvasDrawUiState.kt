package ru.kartollika.yandexcup.canvas

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.Path
import ru.kartollika.yandexcup.canvas.CanvasMode.Draw
import ru.kartollika.yandexcup.canvas.mvi.DrawMode.Erase
import ru.kartollika.yandexcup.canvas.mvi.EditorConfiguration
import ru.kartollika.yandexcup.canvas.mvi.PathProperties
import ru.kartollika.yandexcup.canvas.mvi.PathWithProperties

@Composable
fun rememberCanvasDrawState(
): CanvasDrawUiState {
  return remember {
    CanvasDrawUiState()
  }
}

enum class CanvasMode {
  Draw,
  Transform,
}

@Stable
class CanvasDrawUiState {
  var editorConfiguration by mutableStateOf(EditorConfiguration())
  var currentPath by mutableStateOf<PathWithProperties?>(null)
  var mode by mutableStateOf<CanvasMode>(Draw)

  private var lastOffset: Offset = Offset.Unspecified

  fun startDrawing(offset: Offset) {
    val path = Path()
    val properties = PathProperties(
      color = if (editorConfiguration.currentMode == Erase) {
        Color.Transparent
      } else {
        editorConfiguration.color
      },
      eraseMode = editorConfiguration.currentMode == Erase,
      brushSize = editorConfiguration.brushSizeByMode
    )

    lastOffset = offset
    path.moveTo(offset.x, offset.y)
    currentPath = PathWithProperties(path, properties)
  }

  fun draw(offset: Offset) {
    if (lastOffset != Offset.Unspecified) {
      val newOffset = Offset(
        lastOffset.x + offset.x,
        lastOffset.y + offset.y
      )

      val currentPath = currentPath ?: return
      lastOffset = newOffset
      this.currentPath = currentPath.copy(
        path = currentPath.path.apply {
          lineTo(newOffset.x, newOffset.y)
        },
        properties = currentPath.properties,
        drawIndex = currentPath.drawIndex + 1
      )
    }
  }

  fun transform(matrix: Matrix) {
    val currentPath = currentPath ?: return

    this.currentPath = currentPath.copy(
      path = currentPath.path.apply {
        transform(matrix)
      },
      drawIndex = currentPath.drawIndex + 1
    )
  }
}