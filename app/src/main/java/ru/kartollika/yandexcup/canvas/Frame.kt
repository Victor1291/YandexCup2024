package ru.kartollika.yandexcup.canvas

import android.graphics.Path

data class Frame(
  val paths: List<Path>,
  val pathColors: List<Int>,
)