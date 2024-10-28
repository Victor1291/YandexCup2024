package ru.kartollika.yandexcup.mvi2

import kotlinx.coroutines.flow.StateFlow

interface StateOwner<State : MVIState> {
  val state: StateFlow<State>

  interface Provider<State : MVIState> {
    val stateOwner: StateOwner<State>
  }
}
