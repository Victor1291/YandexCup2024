package ru.kartollika.yandexcup.canvas.mvi

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.AddNewFrame
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.ChangeColor
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.DeleteFrame
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.DrawDrag
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.DrawFinish
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.DrawStart
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.EraseClick
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.OnColorChanged
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.PencilClick
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.RedoChange
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.UndoChange
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.UpdateOffset
import ru.kartollika.yandexcup.canvas.mvi.DrawMode.Erase
import ru.kartollika.yandexcup.canvas.mvi.DrawMode.Pencil
import ru.kartollika.yandexcup.core.replace
import ru.kartollika.yandexcup.mvi2.MVIFeature
import javax.inject.Inject

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

      is DrawFinish -> Unit
      is DrawStart -> Unit
      is UpdateOffset -> Unit
      is EraseClick -> Unit
      is ChangeColor -> Unit
      is PencilClick -> Unit
      is UndoChange -> Unit
      is RedoChange -> Unit
      is OnColorChanged -> Unit
      AddNewFrame -> Unit
      DeleteFrame -> Unit
    }
  }

  override fun reduce(state: CanvasState, action: CanvasAction): CanvasState {
    return when (action) {
      is DrawStart -> {
        val path = Path()
        val properties = PathProperties(
          color = if (state.currentMode == Erase) Color.Transparent else state.color,
          eraseMode = state.currentMode == Erase
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
        currentMode = Erase
      )

      is ChangeColor -> state.copy(
        colorPickerVisible = true,
      )

      PencilClick -> state.copy(
        currentMode = Pencil
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
        color = action.color,
        colorPickerVisible = false
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
      DeleteFrame -> {
        return if (state.frames.size == 1) {
          state.copy(
            frames = persistentListOf(Frame()),
            currentFrameIndex = 0
          )
        } else {
          state.copy(
            frames = state.frames.toMutableList().apply {
              removeLast()
            }.toImmutableList(),
            currentFrameIndex = state.frames.lastIndex - 1
          )
        }
      }
    }
  }
}