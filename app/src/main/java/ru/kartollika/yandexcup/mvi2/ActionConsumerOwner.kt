package ru.kartollika.yandexcup.mvi2

interface ActionConsumerOwner<Action : MVIAction> {
  val actionConsumer: ActionConsumer<Action>
}