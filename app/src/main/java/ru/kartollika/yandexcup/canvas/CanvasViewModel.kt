package ru.kartollika.yandexcup.canvas

import dagger.hilt.android.lifecycle.HiltViewModel
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction
import ru.kartollika.yandexcup.canvas.mvi.CanvasEvent
import ru.kartollika.yandexcup.canvas.mvi.CanvasState
import ru.kartollika.yandexcup.mvi.BaseViewModel
import ru.kartollika.yandexcup.mvi.Reducer
import javax.inject.Inject

@HiltViewModel
class CanvasViewModel @Inject constructor(
) : BaseViewModel<CanvasState, CanvasAction, CanvasEvent>(
  initialState = CanvasState(),
  reducer = CanvasRenderer()
) {
//  fun onTopicClick(topicId: String) {
//
//  }
}

class CanvasRenderer : Reducer<CanvasState, CanvasAction, CanvasEvent> {
  override fun reduce(
    previousState: CanvasState,
    action: CanvasAction
  ): CanvasState {
    return when (action) {
      else -> previousState
    }
  }
}