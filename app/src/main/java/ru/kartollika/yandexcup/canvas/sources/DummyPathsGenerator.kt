package ru.kartollika.yandexcup.canvas.sources

import androidx.compose.ui.graphics.Path
import ru.kartollika.yandexcup.canvas.mvi.EditorConfiguration
import ru.kartollika.yandexcup.canvas.mvi.Frame
import ru.kartollika.yandexcup.canvas.mvi.PathWithProperties
import javax.inject.Inject
import kotlin.random.Random

class DummyPathsGenerator @Inject constructor(
  private val editorConfigurationParser: EditorConfigurationParser,
) {
  fun generateFrames(
    framesCount: Int,
    editorConfiguration: EditorConfiguration,
  ): Collection<Frame> {
    val frames = mutableListOf<Frame>()
    for (i in 0 until framesCount) {
      val frame = Frame()
      frames.add(frame)
    }
    return frames
  }

  private fun generateRandomPaths(editorConfiguration: EditorConfiguration): List<PathWithProperties> {
    val paths = mutableListOf<PathWithProperties>()
    repeat(Random.nextInt(1, 15)) {
      val path = Path()
      val properties = editorConfigurationParser.parseToProperties(editorConfiguration)
      drawRandomInPath(path)
      paths.add(PathWithProperties(path, properties))
    }
    return paths
  }

  private fun drawRandomInPath(
    path: Path,
  ) {
    path.moveTo(
      Random.nextDouble(0.0, 1000.0).toFloat(),
      Random.nextDouble(0.0, 1000.0).toFloat()
    )

    path.lineTo(
      Random.nextDouble(0.0, 1000.0).toFloat(),
      Random.nextDouble(0.0, 1000.0).toFloat()
    )

    path.lineTo(
      Random.nextDouble(0.0, 1000.0).toFloat(),
      Random.nextDouble(0.0, 1000.0).toFloat()
    )

    path.lineTo(
      Random.nextDouble(0.0, 1000.0).toFloat(),
      Random.nextDouble(0.0, 1000.0).toFloat()
    )
  }
}