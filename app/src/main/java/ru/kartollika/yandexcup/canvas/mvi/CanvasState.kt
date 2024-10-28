package ru.kartollika.yandexcup.canvas.mvi

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import ru.kartollika.yandexcup.mvi.Reducer.MVIState

@Immutable
data class CanvasState(
  val canUndo: Boolean = false,
  val canRedo: Boolean = false,
  val currentMode: DrawMode = DrawMode.Pencil,
  val color: Color = Color.Blue,
) : MVIState

@Immutable
sealed interface DrawMode {
  data object Pencil : DrawMode
  data object Erase : DrawMode
  data object Brush : DrawMode
}