package ru.kartollika.yandexcup.canvas.sources

import android.graphics.Color
import android.util.Log
import androidx.compose.ui.graphics.Path
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

class CubePaint(val center: Point3D, val edgeLength: Float) {

    // вершины куба
    private val vertices = mutableListOf(
        Point3D(center.x - edgeLength / 2, center.y - edgeLength / 2, center.z - edgeLength / 2),
        Point3D(center.x + edgeLength / 2, center.y - edgeLength / 2, center.z - edgeLength / 2),
        Point3D(center.x + edgeLength / 2, center.y + edgeLength / 2, center.z - edgeLength / 2),
        Point3D(center.x - edgeLength / 2, center.y + edgeLength / 2, center.z - edgeLength / 2),
        Point3D(center.x - edgeLength / 2, center.y - edgeLength / 2, center.z + edgeLength / 2),
        Point3D(center.x + edgeLength / 2, center.y - edgeLength / 2, center.z + edgeLength / 2),
        Point3D(center.x + edgeLength / 2, center.y + edgeLength / 2, center.z + edgeLength / 2),
        Point3D(center.x - edgeLength / 2, center.y + edgeLength / 2, center.z + edgeLength / 2),
    )

    override fun toString(): String {
        var texti: String = ""
        vertices.forEachIndexed { nomer, vershina ->
            texti += "V$nomer x(${vershina.x}),y(${vershina.y}),z(${vershina.z}) "
            val (vx, vy) = convertTo2D(vershina.x, vershina.y, vershina.z)
            texti += "x($vx), y($vy) \n"
        }
        return texti

    }

    fun getVertices(): MutableList<Point3D> {
        return vertices
    }

    // ребра куба
    private val edges = mutableListOf(
        Pair(vertices[0], vertices[1]),
        Pair(vertices[1], vertices[2]),
        Pair(vertices[2], vertices[3]),
        Pair(vertices[3], vertices[0]),
        Pair(vertices[4], vertices[5]),
        Pair(vertices[5], vertices[6]),
        Pair(vertices[6], vertices[7]),
        Pair(vertices[7], vertices[4]),
        Pair(vertices[0], vertices[4]),
        Pair(vertices[1], vertices[5]),
        Pair(vertices[2], vertices[6]),
        Pair(vertices[3], vertices[7])
    )


    fun convertTo2D(x: Float, y: Float, z: Float): Pair<Float, Float> {
        val newX = x * cos(45.0) + y * cos(135.0) + z * cos(-135.0)
        val newY = x * sin(45.0) + y * sin(135.0) + z * sin(-135.0)
        return Pair(newX.toFloat(), newY.toFloat())
    }

    // вращение куба на угол angle по оси axis
    fun rotate(angle: Float, axis: Point3D) {
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

    private fun randomColorGradient(): Int {
        return Color.HSVToColor(floatArrayOf(Random.nextInt(361).toFloat(), 1f, 1f))
    }
/*
TODO сделать разбивку по кадрам вращая куб
 */

    fun drawPath(path: Path) {

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
