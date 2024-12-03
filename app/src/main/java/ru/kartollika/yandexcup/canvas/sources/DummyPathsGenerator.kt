package ru.kartollika.yandexcup.canvas.sources

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import kotlinx.collections.immutable.toImmutableList
import ru.kartollika.yandexcup.canvas.Shape
import ru.kartollika.yandexcup.canvas.mvi.EditorConfiguration
import ru.kartollika.yandexcup.canvas.mvi.Frame
import ru.kartollika.yandexcup.canvas.mvi.GhostFrame
import ru.kartollika.yandexcup.canvas.mvi.PathProperties
import ru.kartollika.yandexcup.canvas.mvi.PathWithProperties
import ru.kartollika.yandexcup.canvas.mvi.RealFrame
import javax.inject.Inject
import kotlin.random.Random

class DummyPathsGenerator @Inject constructor(
  private val editorConfigurationParser: EditorConfigurationParser,
  private val shapeDrawer: ShapeDrawer,
) {
  fun generateFrames(
    framesCount: Int,
    editorConfiguration: EditorConfiguration,
  ): Collection<Frame> {
    val pathProperties = editorConfigurationParser.parseToProperties(editorConfiguration)
    val canvasWidth = editorConfiguration.canvasSize.width.toDouble()
    val canvasHeight = editorConfiguration.canvasSize.height.toDouble()

    val frames = mutableListOf<Frame>()
    for (i in 0 until framesCount) {
      val frame = GhostFrame(
        creator = {
          RealFrame(
            paths = generateRandomPaths(
              pathProperties = pathProperties,
              canvasWidth = canvasWidth,
              canvasHeight = canvasHeight,
            ).toImmutableList()
          )
        }
      )
      frames.add(frame)
    }

    return frames
  }

  private fun generateRandomPaths(
    pathProperties: PathProperties,
    canvasWidth: Double,
    canvasHeight: Double,
  ): List<PathWithProperties> {
    val paths = mutableListOf<PathWithProperties>()
    repeat(Random.nextInt(1, 6)) {
      val path = Path()
      drawRandomInPath(path, canvasWidth, canvasHeight)
      paths.add(PathWithProperties(path, pathProperties))
    }
    return paths
  }

  private fun drawRandomInPath(
    path: Path,
    canvasWidth: Double,
    canvasHeight: Double,
  ) {
    if (Random.nextBoolean()) {
      path.addPath(
        shapeDrawer.drawShape(randomShape(), 40f).apply {
          translate(
            Offset(
              Random.nextDouble(0.0, canvasWidth).toFloat(),
              Random.nextDouble(0.0, canvasHeight).toFloat()
            ),
          )
        }
      )
    } else {
      path.moveTo(
        Random.nextDouble(0.0, canvasWidth).toFloat(),
        Random.nextDouble(0.0, canvasHeight).toFloat()
      )

      path.lineTo(
        Random.nextDouble(0.0, canvasWidth).toFloat(),
        Random.nextDouble(0.0, canvasHeight).toFloat()
      )

      path.lineTo(
        Random.nextDouble(0.0, canvasWidth).toFloat(),
        Random.nextDouble(0.0, canvasHeight).toFloat()
      )

      path.lineTo(
        Random.nextDouble(0.0, canvasWidth).toFloat(),
        Random.nextDouble(0.0, canvasHeight).toFloat()
      )
    }
  }

  private fun randomShape() = when (Random.nextInt(5)) {
    0 -> Shape.Square
    1 -> Shape.Circle
    2 -> Shape.Triangle
    3 -> Shape.Arrow
    4 -> Shape.Straight
    else -> error("Not supported shape to draw")
  }
}