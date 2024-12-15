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
import javax.inject.Inject

class DummyPathsGenerator4 @Inject constructor(
    private val editorConfigurationParser: EditorConfigurationParser,
    private val shapeDrawer: ShapeDrawer,
) {

    private val cubeRubik =
        CubeRubik2(Point3D(500f, 500f, 500f), CUBE2_SIZE, CUBE2_SIZE, CUBE2_SIZE)
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

        cubeRubik.drawPath(path,2)
        Log.d("rubik", "size rubik = ${cubeRubik.getSize()}")

        if (count < 20) {
            cubeRubik.drawPath(path,1)
            cubeRubik.drawPath(path,0)
            repeat(cubeRubik.getSize()) { num ->
                rotateX(num)
                rotateY(num)
               // rotateZ(num)
            }
            count += 1
        } else if (count < 40) {
            cubeRubik.drawPath(path,0)
            rotateX(2)
            rotateY(2)
            rotateX(0)
            rotateY(0)
            count += 1
        } else {
            rotateX(2)
            rotateY(2)
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