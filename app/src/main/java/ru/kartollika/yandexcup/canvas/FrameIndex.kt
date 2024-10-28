package ru.kartollika.yandexcup.canvas

import androidx.compose.runtime.Immutable

@Immutable
sealed interface FrameIndex {

  @Immutable
  data object Current : FrameIndex

  @Immutable
  data class Index(val index: Int) : FrameIndex
}