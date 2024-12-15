package ru.kartollika.yandexcup.canvas.sources

import android.util.Log
import androidx.compose.ui.graphics.Path
import kotlinx.collections.immutable.toImmutableList
import ru.kartollika.yandexcup.canvas.mvi.EditorConfiguration
import ru.kartollika.yandexcup.canvas.mvi.Frame
import ru.kartollika.yandexcup.canvas.mvi.GhostFrame
import ru.kartollika.yandexcup.canvas.mvi.PathProperties
import ru.kartollika.yandexcup.canvas.mvi.PathWithProperties
import ru.kartollika.yandexcup.canvas.mvi.RealFrame
import ru.kartollika.yandexcup.canvas.sources.cubes.CubeRubik2
import ru.kartollika.yandexcup.canvas.sources.cubes.CubeRubik3
import javax.inject.Inject

class DummyPathsGenerator5 @Inject constructor(
    private val editorConfigurationParser: EditorConfigurationParser,
    private val shapeDrawer: ShapeDrawer,
) {

    private val cubeRubik =
        CubeRubik3(Point3D(500f, 500f, 500f), CUBE2_SIZE, CUBE2_SIZE, CUBE2_SIZE)
    private var dx = -5f // Шаг поворота , на какой угол
    private var dy = 5f
    private var count = 0

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

        val path = Path()

        repeat(cubeRubik.getSize()) { cube ->
            cubeRubik.drawPath(path,cube)
        }

        repeat(cubeRubik.getSize()) { num ->
            rotateX(num)
            rotateY(num)
            // rotateZ(num)
        }

        paths.add(PathWithProperties(path, pathProperties))
        return paths
    }

    companion object {
        const val CUBE2_SIZE = 500f
    }

    private fun rotateX(vertex: Int) {
        cubeRubik.rotate(dy / 100, Point3D(1f, 0f, 0f), vertex)
    }

    private fun rotateY(vertex: Int) {
        cubeRubik.rotate(dy / 100, Point3D(0f, 1f, 0f), vertex)
    }

    private fun rotateZ(vertex: Int) {
        cubeRubik.rotate(dy / 100, Point3D(0f, 0f, 1f), vertex)
    }

    private fun rotateXi(vertex: Int) {
        cubeRubik.rotate(dx / 100, Point3D(1f, 0f, 0f), vertex)
    }

    private fun rotateYi(vertex: Int) {
        cubeRubik.rotate(dx / 100, Point3D(0f, 1f, 0f), vertex)
    }

    private fun rotateZi(vertex: Int) {
        cubeRubik.rotate(dx / 100, Point3D(0f, 0f, 1f), vertex)
    }
}