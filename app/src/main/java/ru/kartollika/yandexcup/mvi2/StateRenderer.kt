package ru.kartollika.yandexcup.mvi2

interface StateRenderer<State : MVIState> {
  fun render(state: State) {}
}
