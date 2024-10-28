package ru.kartollika.yandexcup.canvas.mvi

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import ru.kartollika.yandexcup.mvi2.MVIAction

sealed interface CanvasAction : MVIAction {
  data object DrawFinish : CanvasAction
  data class DrawStart(
    val offset: Offset,
  ) : CanvasAction

  data class DrawDrag(
    val offset: Offset,
  ) : CanvasAction

  data class UpdateOffset(
    val offset: Offset
  ) : CanvasAction

  data object EraseClick : CanvasAction
  data object PencilClick : CanvasAction
  data class ChangeColor(val color: Color) : CanvasAction
  data object UndoChange : CanvasAction
  data object RedoChange : CanvasAction
}