package ru.kartollika.yandexcup.core

import androidx.core.graphics.ColorUtils

fun colorIntToHSL(color: Int): FloatArray {
  val hslOut = floatArrayOf(0f, 0f, 0f)
  ColorUtils.colorToHSL(color, hslOut)
  return hslOut
}