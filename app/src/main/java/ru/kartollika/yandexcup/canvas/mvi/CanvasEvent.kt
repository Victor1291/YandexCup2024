package ru.kartollika.yandexcup.canvas.mvi

import ru.kartollika.yandexcup.mvi2.MVIEvent
import java.io.File

sealed interface CanvasEvent : MVIEvent {
  data class ShareGif(val file: File) : CanvasEvent
  data object ShowExportGifError : CanvasEvent
  data object ShowGenerateDummyFramesError : CanvasEvent
}