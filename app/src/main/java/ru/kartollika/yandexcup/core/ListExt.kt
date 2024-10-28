package ru.kartollika.yandexcup.core

import kotlin.reflect.KProperty1

fun <T> List<T>.replace(condition: (T) -> Boolean, newItem: (T) -> T): List<T> {
  val result = ArrayList<T>(size)
  for (item in this) {
    if (condition(item)) {
      result.add(newItem(item))
    } else {
      result.add(item)
    }
  }
  return result
}

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

fun <T, V> List<T>.replace(key: KProperty1<T, V>, newItem: T): List<T> {
  val result = ArrayList<T>(size)
  for (item in this) {
    if (key.get(item) == key(newItem)) {
      result.add(newItem)
    } else {
      result.add(item)
    }
  }
  return result
}