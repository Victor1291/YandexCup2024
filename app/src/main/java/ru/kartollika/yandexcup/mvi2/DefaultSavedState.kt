package ru.kartollika.yandexcup.mvi2

import androidx.lifecycle.SavedStateHandle

class DefaultSavedState(
  private val savedStateHandle: SavedStateHandle,
    ) : SavedState {
  override fun <T : Any> get(key: String): T? {
    return savedStateHandle.get<T>(key)
  }

  override fun <T : Any> set(
    key: String,
    value: T,
  ) {
    savedStateHandle[key] = value
  }

  override fun contains(key: String): Boolean {
    return savedStateHandle.contains(key)
  }

  override fun remove(key: String) {
    savedStateHandle.remove<Any>(key)
  }
}