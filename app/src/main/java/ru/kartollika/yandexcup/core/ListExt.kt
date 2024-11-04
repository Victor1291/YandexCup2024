package ru.kartollika.yandexcup.core

fun <T> List<T>.replace(replaceIndex: Int, newItem: (T) -> T): List<T> {
  val result = ArrayList<T>(size)
  forEachIndexed { index, item ->
    if (replaceIndex == index) {
      result.add(newItem(item))
    } else {
      result.add(item)
    }
  }
  return result
}