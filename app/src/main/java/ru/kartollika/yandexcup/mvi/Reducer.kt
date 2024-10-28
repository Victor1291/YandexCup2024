package ru.kartollika.yandexcup.mvi

import ru.kartollika.yandexcup.canvas.mvi.CanvasState

interface Reducer<MVIState : Reducer.MVIState, MVIAction : Reducer.MVIAction, MVIEvent : Reducer.MVIEvent> {
    interface MVIState

    interface MVIAction

    interface MVIEvent

    fun reduce(previousState: MVIState, action: MVIAction): CanvasState
}