package ru.kartollika.yandexcup.canvas.mvi

import androidx.compose.ui.graphics.Color
import ru.kartollika.yandexcup.canvas.FrameIndex
import ru.kartollika.yandexcup.canvas.Shape
import ru.kartollika.yandexcup.mvi2.MVIAction

sealed interface CanvasAction : MVIAction {
  // Draw actions
  data class DrawFinish(val pathWithProperties: PathWithProperties) : CanvasAction
  data object DrawStart : CanvasAction

  data object EraseClick : CanvasAction
  data object PencilClick : CanvasAction
  data object OnColorClick : CanvasAction
  data class OnColorChanged(val color: Color) : CanvasAction
  data class OnColorItemClicked(val color: Color) : CanvasAction
  data object UndoChange : CanvasAction
  data object RedoChange : CanvasAction
  data object AddNewFrame : CanvasAction
  data class DeleteFrame(val frameIndex: FrameIndex) : CanvasAction
  data object StartAnimation : CanvasAction
  data object StopAnimation : CanvasAction
  data class ChangeCurrentFrame(val frameIndex: Int) : CanvasAction
  data class AnimationDelayChange(val animationDelay: Float) : CanvasAction
  data object CopyFrame : CanvasAction
  data object ShowFrames : CanvasAction
  data object HideFrames : CanvasAction
  data class SelectFrame(val frameIndex: Int) : CanvasAction
  data object DeleteAllFrames : CanvasAction
  data object HideColorPicker : CanvasAction
  data object ShowColorPicker : CanvasAction
  data object ShowBrushSizePicker : CanvasAction
  data object HideBrushSizePicker : CanvasAction
  data class ChangeBrushSize(val size: Float) : CanvasAction
  data object CustomColorClick : CanvasAction
  data object OpenShapes : CanvasAction
  data class SelectShape(val shape: Shape) : CanvasAction
  data object ExportToGif : CanvasAction
  data class GenerateDummyFrames(val framesCount: Int) : CanvasAction
  data class AddFrames(val frames: Collection<Frame>) : CanvasAction
}