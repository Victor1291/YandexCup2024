package ru.kartollika.yandexcup.mvi2

interface EventsReceiver<Event : MVIEvent> {
  fun onEvent(event: Event) {}
}
