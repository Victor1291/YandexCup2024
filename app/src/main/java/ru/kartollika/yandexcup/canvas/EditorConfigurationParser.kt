package ru.kartollika.yandexcup.canvas

import ru.kartollika.yandexcup.canvas.mvi.DrawMode.Erase
import ru.kartollika.yandexcup.canvas.mvi.EditorConfiguration
import ru.kartollika.yandexcup.canvas.mvi.PathProperties
import javax.inject.Inject

class EditorConfigurationParser @Inject constructor() {
  fun parseToProperties(
    editorConfiguration: EditorConfiguration,
  ): PathProperties {
    val properties = PathProperties(
      color = editorConfiguration.color,
      eraseMode = editorConfiguration.currentMode == Erase,
      brushSize = editorConfiguration.brushSize
    )
    return properties
  }
}