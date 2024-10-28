package ru.kartollika.yandexcup.canvas.mvi

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion
import androidx.compose.ui.graphics.Path
import kotlinx.collections.immutable.toImmutableList
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.ChangeColor
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.DrawDrag
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.DrawFinish
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.DrawStart
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.EraseClick
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.PencilClick
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.UndoChange
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.UpdateOffset
import ru.kartollika.yandexcup.canvas.mvi.DrawMode.Erase
import ru.kartollika.yandexcup.canvas.mvi.DrawMode.Pencil
import ru.kartollika.yandexcup.mvi2.MVIFeature
import javax.inject.Inject

class CanvasFeature @Inject constructor(

) : MVIFeature<CanvasState, CanvasAction, CanvasEvent>() {
  override fun initialState(): CanvasState = CanvasState()

  override suspend fun processAction(state: CanvasState, action: CanvasAction) {
    when (action) {
      is DrawDrag -> {
        val lastOffset = state.lastOffset
        if (lastOffset != Offset.Unspecified) {
          val newOffset = Offset(
            lastOffset.x + action.offset.x,
            lastOffset.y + action.offset.y
          )
          state.currentPath?.path?.lineTo(newOffset.x, newOffset.y)
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
          currentPath = PathWithProperties(
            path = path,
            properties = properties
          ),
          lastOffset = action.offset
        )
      }

      is DrawDrag -> state
      is UpdateOffset -> {
        state.copy(
          lastOffset = action.offset
        )
      }

      DrawFinish -> {
        val paths = state.paths.toMutableList().apply {
          state.currentPath?.let(this::add)
        }.toImmutableList()

        state.copy(
          paths = paths,
          currentPath = null,
          canUndo = paths.isNotEmpty()
        )
      }

      EraseClick -> state.copy(
        currentMode = Erase
      )

      is ChangeColor -> state.copy(
        color = action.color
      )

      PencilClick -> state.copy(
        currentMode = Pencil
      )

      UndoChange -> {
        val paths = state.paths.toMutableList().apply {
          removeAt(state.paths.lastIndex)
        }.toImmutableList()

        state.copy(
          paths = paths,
          canUndo = paths.isNotEmpty()
        )
      }
    }
  }
}