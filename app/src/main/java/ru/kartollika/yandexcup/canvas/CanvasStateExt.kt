package ru.kartollika.yandexcup.canvas

import androidx.compose.ui.graphics.Color
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import ru.kartollika.yandexcup.canvas.FrameIndex.Current
import ru.kartollika.yandexcup.canvas.FrameIndex.Index
import ru.kartollika.yandexcup.canvas.mvi.CanvasState
import ru.kartollika.yandexcup.canvas.mvi.DrawMode
import ru.kartollika.yandexcup.canvas.mvi.Frame
import ru.kartollika.yandexcup.canvas.mvi.RealFrame

fun CanvasState.mutateFrames(block: MutableList<Frame>.() -> Unit) =
  frames.toMutableList().apply(block).toImmutableList()

fun CanvasState.copyFrame() = copy(
  frames = mutateFrames {
    add(
      currentFrame
        .materialize()
        .copy(
          snapshots = persistentListOf(),
        )
    )
  },
  currentFrameIndex = frames.lastIndex + 1
)

fun CanvasState.addNewFrame(): CanvasState {
  val newFrame = RealFrame()
  return copy(
    frames = mutateFrames {
      add(newFrame)
    },
    currentFrameIndex = frames.lastIndex + 1
  )
}

fun CanvasState.deleteFrame(frameIndex: FrameIndex): CanvasState {
  return if (frames.size == 1) {
    copy(
      frames = persistentListOf(RealFrame()),
      currentFrameIndex = 0
    )
  } else {
    val indexToRemove = when (frameIndex) {
      is Current -> currentFrameIndex
      is Index -> frameIndex.index
    }
    copy(
      frames = mutateFrames {
        removeAt(indexToRemove)
      },
      currentFrameIndex = frames.lastIndex - 1
    )
  }
}

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
  shapesPickerVisible: Boolean = editorConfiguration.shapesPickerVisible,
  dummyFramesCountInputVisible: Boolean = editorConfiguration.dummyFramesCountInputVisible,
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
    shapesPickerVisible = shapesPickerVisible,
    dummyFramesCountInputVisible = dummyFramesCountInputVisible
  )
)