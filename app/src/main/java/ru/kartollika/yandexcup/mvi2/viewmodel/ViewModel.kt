package ru.kartollika.yandexcup.mvi2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ru.kartollika.yandexcup.mvi2.ActionConsumer
import ru.kartollika.yandexcup.mvi2.ActionConsumerOwner
import ru.kartollika.yandexcup.mvi2.EventsOwner
import ru.kartollika.yandexcup.mvi2.MVIAction
import ru.kartollika.yandexcup.mvi2.MVIEvent
import ru.kartollika.yandexcup.mvi2.MVIFeature
import ru.kartollika.yandexcup.mvi2.MVIState
import ru.kartollika.yandexcup.mvi2.SavedState
import ru.kartollika.yandexcup.mvi2.StateOwner

abstract class ViewModel<State : MVIState, Action : MVIAction, Event : MVIEvent>(
  private val mviFeature: MVIFeature<State, Action, Event>,
  savedState: SavedState? = null,
) : ViewModel(),
    StateOwner.Provider<State>,
    EventsOwner.Provider<Event>,
  ActionConsumerOwner<Action> {

  override val actionConsumer: ActionConsumer<Action>
    get() = mviFeature

  override val stateOwner: StateOwner<State>
    get() = mviFeature

  override val eventsOwner: EventsOwner<Event>
    get() = mviFeature

  init {
    mviFeature.coroutineScope = viewModelScope
    mviFeature.initSavedState(savedState)
    mviFeature.startFeature()
  }
}
