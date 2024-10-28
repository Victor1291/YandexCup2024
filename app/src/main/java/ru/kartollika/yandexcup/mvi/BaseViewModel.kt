package ru.kartollika.yandexcup.mvi

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class BaseViewModel<State : Reducer.MVIState, Action : Reducer.MVIAction, Event : Reducer.MVIEvent>(
  initialState: State,
  private val reducer: Reducer<State, Action, Event>
) : ViewModel() {
  private val _state: MutableStateFlow<State> = MutableStateFlow(initialState)
  val state: StateFlow<State>
    get() = _state.asStateFlow()

  private val _actions: MutableSharedFlow<Action> = MutableSharedFlow()
  val event: SharedFlow<Action>
    get() = _actions.asSharedFlow()

  private val _events = MutableSharedFlow<Event>()
  val effect = _events.asSharedFlow()

  fun sendEffect(effect: Event) {
    _events.tryEmit(effect)
  }

  fun sendAction(event: Action) {
    val (newState, _) = reducer.reduce(_state.value, event)
    _state.tryEmit(newState)
  }
}