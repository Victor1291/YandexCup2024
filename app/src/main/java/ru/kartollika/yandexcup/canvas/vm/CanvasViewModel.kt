package ru.kartollika.yandexcup.canvas.vm

import dagger.hilt.android.lifecycle.HiltViewModel
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction
import ru.kartollika.yandexcup.canvas.mvi.CanvasEvent
import ru.kartollika.yandexcup.canvas.mvi.CanvasFeature
import ru.kartollika.yandexcup.canvas.mvi.CanvasState
import ru.kartollika.yandexcup.canvas.mvi2.viewmodel.ViewModel
import javax.inject.Inject

@HiltViewModel
class CanvasViewModel @Inject constructor(
  feature: CanvasFeature,
) : ViewModel<CanvasState, CanvasAction, CanvasEvent>(
  mviFeature = feature
)