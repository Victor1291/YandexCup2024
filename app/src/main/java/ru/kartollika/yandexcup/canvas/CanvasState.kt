package ru.kartollika.yandexcup.canvas

import androidx.compose.runtime.Immutable

@Immutable
data class CanvasState(
  val canUndo: Boolean = false,
  val canRedo: Boolean = false,
)