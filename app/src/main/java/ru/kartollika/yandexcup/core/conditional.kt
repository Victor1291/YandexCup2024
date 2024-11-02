package ru.kartollika.yandexcup.core

import androidx.compose.ui.Modifier

fun Modifier.conditional(
  condition: Boolean,
  modifier: Modifier.() -> Modifier,
): Modifier {
  return if (condition) {
    this then modifier()
  } else {
    this
  }
}