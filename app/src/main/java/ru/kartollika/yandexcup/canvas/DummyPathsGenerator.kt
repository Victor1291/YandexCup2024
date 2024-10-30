package ru.kartollika.yandexcup.canvas

import androidx.compose.ui.graphics.Path
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import ru.kartollika.yandexcup.canvas.mvi.EditorConfiguration
import ru.kartollika.yandexcup.canvas.mvi.Frame
import ru.kartollika.yandexcup.canvas.mvi.PathWithProperties
import javax.inject.Inject
import kotlin.random.Random

class DummyPathsGenerator @Inject constructor(
  private val editorConfigurationParser: EditorConfigurationParser,
) {
  suspend fun generateFrames(
    framesCount: Int,
    editorConfiguration: EditorConfiguration,
  ): List<Frame> = coroutineScope {
    val framesDeferred = mutableListOf<Deferred<Frame>>()
    for (i in 0 until framesCount) {
      val async = async {
        val paths = generateRandomPaths(editorConfiguration)

        Frame(
          paths = paths.toImmutableList()
        )
      }
      framesDeferred.add(async)
    }

    return@coroutineScope framesDeferred.awaitAll()
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