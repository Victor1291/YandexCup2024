package ru.kartollika.yandexcup.core

import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp

fun Dp.toPx(density: Density): Float =
  with(density) { toPx() }

fun Int.toDp(density: Density): Dp =
  with(density) { toDp() }