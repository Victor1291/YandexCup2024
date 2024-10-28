package ru.kartollika.yandexcup.mvi2

interface EventConsumer<Event : MVIEvent> {
  fun consumeEvent(event: Event)
}