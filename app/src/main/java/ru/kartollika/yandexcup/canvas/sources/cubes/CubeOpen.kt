package ru.kartollika.yandexcup.canvas.sources.cubes


import ru.kartollika.yandexcup.canvas.sources.Point3D
import kotlin.math.cos
import kotlin.math.sin

private const val TAG = "cube"

open class CubeOpen(
    val center: Point3D,
    private val edgeX: Float,
    private val edgeY: Float,
    private val edgeZ: Float
) {

    // вершины куба
    open val vertices = mutableListOf(
        Point3D(center.x - edgeX / 2, center.y - edgeY / 2, center.z - edgeZ / 2), //1  - - -
        Point3D(center.x + edgeX / 2, center.y - edgeY / 2, center.z - edgeZ / 2), //2. + - -
        Point3D(center.x + edgeX / 2, center.y + edgeY / 2, center.z - edgeZ / 2), //3. + + -
        Point3D(center.x - edgeX / 2, center.y + edgeY / 2, center.z - edgeZ / 2), //4. - + -
        Point3D(center.x - edgeX / 2, center.y - edgeY / 2, center.z + edgeZ / 2), //5. - - +
        Point3D(center.x + edgeX / 2, center.y - edgeY / 2, center.z + edgeZ / 2), //6. + - +
        Point3D(center.x + edgeX / 2, center.y + edgeY / 2, center.z + edgeZ / 2), //7. + + +
        Point3D(center.x - edgeX / 2, center.y + edgeY / 2, center.z + edgeZ / 2), //8. - + +
    )

    open fun convertTo2D(x: Float, y: Float, z: Float): Pair<Float, Float> {
        val newX = x * cos(45.0) + y * cos(135.0) + z * cos(-135.0)
        val newY = x * sin(45.0) + y * sin(135.0) + z * sin(-135.0)
        return Pair(newX.toFloat(), newY.toFloat())
    }

    // вращение куба на угол angle по оси axis
    open fun rotate(angle: Float, axis: Point3D) {
        val sin = sin(angle)
        val cos = cos(angle)
        vertices.forEach {
            val x = it.x - center.x
            val y = it.y - center.y
            val z = it.z - center.z
            when (axis) {
                Point3D(1f, 0f, 0f) -> {
                    val resY = y * cos - z * sin
                    val resZ = y * sin + z * cos
                    it.y = resY + center.y
                    it.z = resZ + center.z
                }

                Point3D(0f, 1f, 0f) -> {
                    val resZ = z * cos - x * sin
                    val resX = z * sin + x * cos
                    it.z = resZ + center.z
                    it.x = resX + center.x
                }

                Point3D(0f, 0f, 1f) -> {
                    val resX = x * cos - y * sin
                    val resY = x * sin + y * cos
                    it.x = resX + center.x
                    it.y = resY + center.y
                }
            }
        }
    }

    open fun drawPath(path: androidx.compose.ui.graphics.Path) {

        path.moveTo(vertices[0].x, vertices[0].y)
        for (i in 1..3) {
            path.lineTo(vertices[i].x, vertices[i].y)
        }
        path.lineTo(vertices[0].x, vertices[0].y)

        path.moveTo(vertices[4].x, vertices[4].y)
        for (i in 5..7) {
            path.lineTo(vertices[i].x, vertices[i].y)
        }
        path.lineTo(vertices[4].x, vertices[4].y)

        for (i in 0..3) {
            path.moveTo(vertices[i].x, vertices[i].y)
            path.lineTo(vertices[i + 4].x, vertices[i + 4].y)
        }
    }
}