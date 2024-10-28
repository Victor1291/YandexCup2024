package ru.kartollika.yandexcup.mvi2

interface SavedState {
  fun <T : Any> get(key: String): T?

  fun <T : Any> set(key: String, value: T)

  fun contains(key: String): Boolean

  fun remove(key: String)
}