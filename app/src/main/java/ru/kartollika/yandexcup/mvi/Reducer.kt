package ru.kartollika.yandexcup.mvi

interface Reducer<MVIState : Reducer.MVIState, MVIAction : Reducer.MVIAction, MVIEvent : Reducer.MVIEvent> {
    interface MVIState

    interface MVIAction

    interface MVIEvent

    fun reduce(previousState: MVIState, action: MVIAction): MVIState
}