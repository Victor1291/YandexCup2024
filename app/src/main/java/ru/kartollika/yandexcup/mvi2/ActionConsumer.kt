package ru.kartollika.yandexcup.mvi2

interface ActionConsumer<Action : MVIAction> {
  fun consumeAction(action: Action)
}