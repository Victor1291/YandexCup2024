package ru.kartollika.yandexcup.canvas.sources

import androidx.compose.ui.graphics.Path
import kotlinx.collections.immutable.toImmutableList
import ru.kartollika.yandexcup.canvas.mvi.EditorConfiguration
import ru.kartollika.yandexcup.canvas.mvi.Frame
import ru.kartollika.yandexcup.canvas.mvi.GhostFrame
import ru.kartollika.yandexcup.canvas.mvi.PathProperties
import ru.kartollika.yandexcup.canvas.mvi.PathWithProperties
import ru.kartollika.yandexcup.canvas.mvi.RealFrame
import javax.inject.Inject

class DummyPathsGenerator2 @Inject constructor(
  private val editorConfigurationParser: EditorConfigurationParser,
  private val shapeDrawer: ShapeDrawer,
) {

  private val cube = Cube(Point3D(500f, 500f, 500f), 500f)
  private var dx = 5f
  private var dy = 5f

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
   // repeat(Random.nextInt(1, 6)) {
      val path = Path()
     // drawRandomInPath(path, canvasWidth, canvasHeight)
      cube.drawPath(path)
      cube.rotate(dy / 100, Point3D(0f, 0f, 1f))
      cube.rotate(dy / 100, Point3D(1f, 0f, 0f))
      cube.rotate(-dx / 100, Point3D(0f, 1f, 0f))
      paths.add(PathWithProperties(path, pathProperties))
     // dx += 5f
   // }
    return paths
  }

}