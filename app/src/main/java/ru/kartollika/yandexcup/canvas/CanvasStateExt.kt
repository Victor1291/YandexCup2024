package ru.kartollika.yandexcup.canvas

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntSize
import ru.kartollika.yandexcup.canvas.mvi.CanvasState
import ru.kartollika.yandexcup.canvas.mvi.DrawMode

fun CanvasState.hidePickers(): CanvasState = updateEditorConfig(
  colorPickerVisible = false,
  brushSizePickerVisible = false,
  shapesPickerVisible = false,
)

fun CanvasState.openColorPicker(): CanvasState = updateEditorConfig(
  colorPickerVisible = true,
  brushSizePickerVisible = false,
  shapesPickerVisible = false,
)

fun CanvasState.openBrushPicker(): CanvasState = updateEditorConfig(
  colorPickerVisible = false,
  brushSizePickerVisible = true,
  shapesPickerVisible = false,
)

fun CanvasState.openShapesPicker(): CanvasState = updateEditorConfig(
  colorPickerVisible = false,
  brushSizePickerVisible = false,
  shapesPickerVisible = true,
)

fun CanvasState.updateEditorConfig(
  isPreviewAnimation: Boolean = editorConfiguration.isPreviewAnimation,
  color: Color = editorConfiguration.color,
  colorPickerVisible: Boolean = editorConfiguration.colorPickerVisible,
  colorPickerExpanded: Boolean = editorConfiguration.colorPickerExpanded,
  brushSizePickerVisible: Boolean = editorConfiguration.brushSizePickerVisible,
  currentMode: DrawMode = editorConfiguration.currentMode,
  animationDelay: Int = editorConfiguration.animationDelay,
  brushSize: Float = editorConfiguration.brushSize,
  rotateAngle: Float = editorConfiguration.rotateAngle,
  eraserSize: Float = editorConfiguration.eraserSize,
  shapesPickerVisible: Boolean = editorConfiguration.shapesPickerVisible,
  dummyFramesCountInputVisible: Boolean = editorConfiguration.dummyFramesCountInputVisible,
  canvasSize: IntSize = editorConfiguration.canvasSize,
  isLoading: Boolean = editorConfiguration.isLoading,
) = copy(
  editorConfiguration = editorConfiguration.copy(
    isPreviewAnimation = isPreviewAnimation,
    color = color,
    colorPickerVisible = colorPickerVisible,
    colorPickerExpanded = colorPickerExpanded,
    brushSizePickerVisible = brushSizePickerVisible,
    currentMode = currentMode,
    animationDelay = animationDelay,
    brushSize = brushSize,
    eraserSize = eraserSize,
    shapesPickerVisible = shapesPickerVisible,
    dummyFramesCountInputVisible = dummyFramesCountInputVisible,
    canvasSize = canvasSize,
    isLoading = isLoading
  )
)