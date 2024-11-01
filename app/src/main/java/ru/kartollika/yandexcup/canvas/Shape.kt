package ru.kartollika.yandexcup.canvas

sealed interface Shape {
  data object Square : Shape
  data object Circle : Shape
  data object Triangle : Shape
  data object Straight : Shape
  data object Arrow : Shape
}