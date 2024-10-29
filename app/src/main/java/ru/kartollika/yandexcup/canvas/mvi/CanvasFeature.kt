package ru.kartollika.yandexcup.canvas.mvi

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.kartollika.yandexcup.canvas.FrameIndex.Current
import ru.kartollika.yandexcup.canvas.FrameIndex.Index
import ru.kartollika.yandexcup.canvas.Shape
import ru.kartollika.yandexcup.canvas.Shape.Circle
import ru.kartollika.yandexcup.canvas.Shape.Square
import ru.kartollika.yandexcup.canvas.Shape.Triangle
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.AddNewFrame
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.AnimationDelayChange
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.ChangeBrushSize
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.ChangeCurrentFrame
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.CopyFrame
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.CustomColorClick
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.DeleteAllFrames
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.DeleteFrame
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.DrawDrag
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.DrawFinish
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.DrawPath
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.DrawStart
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.EraseClick
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.HideBrushSizePicker
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.HideColorPicker
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.HideFrames
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.OnColorChanged
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.OnColorClick
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.OnColorItemClicked
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.OpenShapes
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.PencilClick
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.RedoChange
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.SelectFrame
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.SelectShape
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.ShowBrushSizePicker
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.ShowColorPicker
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.ShowFrames
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.StartAnimation
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.StopAnimation
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.UndoChange
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.UpdateOffset
import ru.kartollika.yandexcup.canvas.mvi.DrawMode.Erase
import ru.kartollika.yandexcup.canvas.mvi.DrawMode.Pencil
import ru.kartollika.yandexcup.core.replace
import ru.kartollika.yandexcup.mvi2.MVIFeature
import javax.inject.Inject
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.milliseconds

class CanvasFeature @Inject constructor(

) : MVIFeature<CanvasState, CanvasAction, CanvasEvent>() {
  override fun initialState(): CanvasState = CanvasState()

  override suspend fun processAction(state: CanvasState, action: CanvasAction) {
    when (action) {
      is DrawDrag -> {
        val frame = state.currentFrame

        val lastOffset = frame.lastOffset
        if (lastOffset != Offset.Unspecified) {
          val newOffset = Offset(
            lastOffset.x + action.offset.x,
            lastOffset.y + action.offset.y
          )
          frame.currentPath?.path?.lineTo(newOffset.x, newOffset.y)
          consumeAction(UpdateOffset(newOffset))
        }
      }

      is StartAnimation -> {
        consumeAction(HideColorPicker)
        consumeAction(HideBrushSizePicker)
        startFramesAnimation()
      }

      is DrawFinish -> Unit
      is DrawStart -> {
        consumeAction(HideColorPicker)
      }

      is UpdateOffset -> Unit
      is EraseClick -> {
        consumeAction(HideColorPicker)
      }

      is OnColorClick -> {
        consumeAction(HideBrushSizePicker)
        if (state.editorConfiguration.colorPickerVisible) {
          consumeAction(HideColorPicker)
        } else {
          consumeAction(ShowColorPicker)
        }
      }

      is PencilClick -> {
        consumeAction(HideColorPicker)
      }

      is UndoChange -> Unit
      is RedoChange -> Unit
      is OnColorChanged -> Unit
      is AddNewFrame -> Unit
      is DeleteFrame -> Unit
      is StopAnimation -> Unit
      is ChangeCurrentFrame -> Unit
      is AnimationDelayChange -> Unit
      CopyFrame -> Unit
      ShowFrames -> Unit
      HideFrames -> Unit
      is SelectFrame -> Unit
      is DeleteAllFrames -> Unit
      is HideColorPicker -> Unit
      ShowBrushSizePicker -> {
        consumeAction(HideColorPicker)
      }

      is ChangeBrushSize -> Unit
      HideBrushSizePicker -> Unit
      CustomColorClick -> Unit
      ShowColorPicker -> {
        consumeAction(HideBrushSizePicker)
      }

      is OnColorItemClicked -> {
        consumeAction(HideColorPicker)
      }

      OpenShapes -> Unit
      is SelectShape -> drawShape(action.shape)
      is DrawPath -> Unit
    }
  }

  private fun drawShape(shape: Shape) {
    val path = Path()

    when (shape) {
      Circle -> path.addOval(Rect(100f, 100f, 400f, 400f))
      Square -> path.addRect(Rect(100f, 100f, 400f, 400f))
      Triangle -> {
        path.moveTo(100f, 100f)
        path.lineTo(200f, 250f)
        path.lineTo(0f, 250f)
        path.lineTo(100f, 100f)
      }
    }
    consumeAction(DrawPath(path))
  }

  private suspend fun startFramesAnimation() = coroutineScope {
    launch {
      var frameIndex = 0
      while (state.value.editorConfiguration.isPreviewAnimation) {
        consumeAction(ChangeCurrentFrame(frameIndex))
        delay(state.value.editorConfiguration.animationDelay.milliseconds)
        frameIndex = (frameIndex + 1) % state.value.frames.size
      }
    }
  }

  override fun reduce(state: CanvasState, action: CanvasAction): CanvasState {
    return when (action) {
      is DrawStart -> {
        if (state.editorConfiguration.isPreviewAnimation) return state

        val path = Path()
        val properties = PathProperties(
          color = if (state.editorConfiguration.currentMode == Erase) {
            Color.Transparent
          } else {
            state.editorConfiguration.color
          },
          eraseMode = state.editorConfiguration.currentMode == Erase,
          brushSize = state.editorConfiguration.brushSize
        )

        path.moveTo(action.offset.x, action.offset.y)
        state.copy(
          frames = state.frames.replace(
            replaceIndex = state.currentFrameIndex,
            newItem = { frame ->
              frame.copy(
                currentPath = PathWithProperties(
                  path = path,
                  properties = properties
                ),
                lastOffset = action.offset
              )
            }
          ).toImmutableList(),
          editorConfiguration = state.editorConfiguration
        )
      }

      is DrawDrag -> state
      is UpdateOffset -> {
        state.copy(
          frames = state.frames.replace(
            replaceIndex = state.currentFrameIndex,
            newItem = { frame: Frame ->
              frame.copy(
                lastOffset = action.offset
              )
            }
          ).toImmutableList()
        )
      }

      DrawFinish -> {
        val paths = state.currentFrame.paths.toMutableList().apply {
          state.currentFrame.currentPath?.let(this::add)
        }.toImmutableList()

        state.copy(
          frames = state.frames.replace(
            replaceIndex = state.currentFrameIndex,
            newItem = { frame ->
              frame.copy(
                paths = paths,
                currentPath = null,
                undoPaths = persistentListOf()
              )
            }
          ).toImmutableList(),
        )
      }

      EraseClick -> state.copy(
        editorConfiguration = state.editorConfiguration.copy(
          currentMode = Erase
        ),
      )

      is OnColorClick -> state
      is ShowColorPicker -> state.copy(
        editorConfiguration = state.editorConfiguration.copy(
          colorPickerVisible = !state.editorConfiguration.colorPickerVisible,
        )
      )

      PencilClick -> state.copy(
        editorConfiguration = state.editorConfiguration.copy(
          currentMode = Pencil
        )
      )

      UndoChange -> {
        val undoPath: PathWithProperties

        val paths = state.currentFrame.paths.toMutableList().apply {
          undoPath = removeLast()
        }.toImmutableList()

        val undoPaths = state.currentFrame.undoPaths.toMutableList().apply {
          add(undoPath)
        }.toImmutableList()

        state.copy(
          frames = state.frames.replace(
            replaceIndex = state.currentFrameIndex,
            newItem = { frame ->
              frame.copy(
                paths = paths,
                undoPaths = undoPaths,
              )
            }
          ).toImmutableList(),
        )
      }

      RedoChange -> {
        val redoPath = state.currentFrame.undoPaths.lastOrNull() ?: return state

        val paths = state.currentFrame.paths.toMutableList().apply {
          add(redoPath)
        }.toImmutableList()

        val undoPaths = state.currentFrame.undoPaths.toMutableList().apply {
          removeLast()
        }.toImmutableList()

        state.copy(
          frames = state.frames.replace(
            replaceIndex = state.currentFrameIndex,
            newItem = { frame ->
              frame.copy(
                paths = paths,
                undoPaths = undoPaths,
              )
            }
          ).toImmutableList(),
        )
      }

      is OnColorChanged -> state.copy(
        editorConfiguration = state.editorConfiguration.copy(
          color = action.color,
        )
      )

      AddNewFrame -> {
        val newFrame = Frame()
        state.copy(
          frames = state.frames.toMutableList().apply {
            add(newFrame)
          }.toImmutableList(),
          currentFrameIndex = state.frames.lastIndex + 1
        )
      }

      is DeleteFrame -> {
        return if (state.frames.size == 1) {
          state.copy(
            frames = persistentListOf(Frame()),
            currentFrameIndex = 0
          )
        } else {
          val indexToRemove = when (val frameIndex = action.frameIndex) {
            is Current -> state.currentFrameIndex
            is Index -> frameIndex.index
          }
          state.copy(
            frames = state.frames.toMutableList().apply {
              removeAt(indexToRemove)
            }.toImmutableList(),
            currentFrameIndex = state.frames.lastIndex - 1
          )
        }
      }

      StartAnimation -> state.copy(
        editorConfiguration = state.editorConfiguration.copy(
          isPreviewAnimation = true
        )
      )

      StopAnimation -> state.copy(
        editorConfiguration = state.editorConfiguration.copy(
          isPreviewAnimation = false,
        ),
        currentFrameIndex = state.frames.lastIndex
      )

      is ChangeCurrentFrame -> state.copy(
        currentFrameIndex = action.frameIndex
      )

      is AnimationDelayChange -> state.copy(
        editorConfiguration = state.editorConfiguration.copy(
          animationDelay = action.animationDelay.roundToInt()
        )
      )

      CopyFrame -> state.copy(
        frames = state.frames.toMutableList().apply {
          add(
            state.currentFrame.copy(
              undoPaths = persistentListOf()
            )
          )
        }.toImmutableList(),
        currentFrameIndex = state.frames.lastIndex + 1
      )

      ShowFrames -> state.copy(
        framesSheetVisible = true
      )

      HideFrames -> state.copy(
        framesSheetVisible = false
      )

      is SelectFrame -> state.copy(
        currentFrameIndex = action.frameIndex,
        framesSheetVisible = false
      )

      DeleteAllFrames -> state.copy(
        frames = persistentListOf(Frame()),
        currentFrameIndex = 0
      )

      is HideColorPicker -> state.copy(
        editorConfiguration = state.editorConfiguration.copy(
          colorPickerVisible = false,
          colorPickerExpanded = false
        )
      )

      ShowBrushSizePicker -> state.copy(
        editorConfiguration = state.editorConfiguration.copy(
          brushSizePickerVisible = !state.editorConfiguration.brushSizePickerVisible
        )
      )

      is ChangeBrushSize -> state.copy(
        editorConfiguration = state.editorConfiguration.copy(
          brushSize = action.size
        )
      )

      HideBrushSizePicker -> state.copy(
        editorConfiguration = state.editorConfiguration.copy(
          brushSizePickerVisible = false
        )
      )

      CustomColorClick -> state.copy(
        editorConfiguration = state.editorConfiguration.copy(
          colorPickerExpanded = !state.editorConfiguration.colorPickerExpanded
        )
      )

      is OnColorItemClicked -> state.copy(
        editorConfiguration = state.editorConfiguration.copy(
          color = action.color
        )
      )

      OpenShapes -> state.copy(
        editorConfiguration = state.editorConfiguration.copy(
          shapesPickerVisible = !state.editorConfiguration.shapesPickerVisible
        )
      )

      is SelectShape -> state
      is DrawPath -> state.copy(
        frames = state.frames.replace(
          replaceIndex = state.currentFrameIndex,
          newItem = { frame ->
            val properties = PathProperties(
              color = state.editorConfiguration.color,
              eraseMode = false,
              brushSize = state.editorConfiguration.brushSize
            )

            frame.copy(
              paths = frame.paths.toMutableList().apply {
                add(
                  PathWithProperties(
                    path = action.path,
                    properties = properties
                  )
                )
              }.toImmutableList()
            )
          }
        ).toImmutableList()
      )
    }
  }
}