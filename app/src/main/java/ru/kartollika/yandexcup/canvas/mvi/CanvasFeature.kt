package ru.kartollika.yandexcup.canvas.mvi

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import kotlinx.collections.immutable.toImmutableList
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.ChangeColor
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.DrawDrag
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.DrawFinish
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.DrawStart
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.EraseClick
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.UpdateOffset
import ru.kartollika.yandexcup.canvas.mvi.DrawMode.Erase
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

      DrawFinish -> Unit
      is DrawStart -> Unit
      is UpdateOffset -> Unit
      EraseClick -> Unit
      is ChangeColor -> Unit
    }
  }

  override fun reduce(state: CanvasState, action: CanvasAction): CanvasState {
    return when (action) {
      is DrawStart -> {
        val path = Path()
        val properties = PathProperties(
          color = state.color
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
        state.copy(
          paths = state.paths.toMutableList().apply {
            state.currentPath?.let(this::add)
          }.toImmutableList(),
          currentPath = null
        )
      }

      EraseClick -> state.copy(
        currentMode = Erase
      )

      is ChangeColor -> state.copy(
        color = action.color
      )
    }
  }
}