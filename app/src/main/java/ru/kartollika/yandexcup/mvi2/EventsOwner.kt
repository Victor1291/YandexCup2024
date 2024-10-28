package ru.kartollika.yandexcup.mvi2

import kotlinx.coroutines.flow.SharedFlow

interface EventsOwner<Event : MVIEvent> {
  val events: SharedFlow<Event>

  interface Provider<Event : MVIEvent> {
    val eventsOwner: EventsOwner<Event>
  }
}