package ru.kartollika.yandexcup.canvas.sources.cubes

import android.util.Log
import ru.kartollika.yandexcup.canvas.sources.Point3D
import kotlin.math.cos
import kotlin.math.sin

class CubeRubik3(center: Point3D, edgeX: Float, edgeY: Float, edgeZ: Float) :
    CubeOpen(center, edgeX, edgeY, edgeZ) {

    //массив вершин
    private var verticesList = mutableListOf<MutableList<Point3D>>()
    private var vertices2 = vertices


    init {
        initVertices(edgeX, edgeY, edgeZ)
    }

    fun getSize(): Int {
        return verticesList.size
    }

    private fun initVertices(edgeX: Float, edgeY: Float, edgeZ: Float) {
        val eX = edgeX.toInt()
        val stepX = (edgeX / 3)

        val centerMinusX = center.copy(x = center.x - stepX - 50f)
        val centerPlusX = center.copy(x = center.x + stepX + 50f)
        val centerMinusY = center.copy(y = center.y - stepX - 50f)
        val centerPlusY = center.copy(y = center.y + stepX + 50f)
        val centerMinusZ = center.copy(z = center.z - stepX - 50f)
        val centerPlusZ = center.copy(z = center.z + stepX + 50f)

        modelVertices(center, stepX, stepX, stepX)
        modelVertices(centerPlusX, stepX, stepX, stepX)
        modelVertices(centerMinusX, stepX, stepX, stepX)
        modelVertices(centerPlusZ, stepX, stepX, stepX)
        modelVertices(centerMinusZ, stepX, stepX, stepX)


        val centerMinusXMinusY = center.copy(x = center.x - stepX - 50f, y = center.y - stepX - 50f)
        val centerMinusXPlusY = center.copy(x = center.x - stepX - 50f, y = center.y + stepX + 50f)
        val centerPlusXMinusY = center.copy(x = center.x + stepX + 50f, y = center.y - stepX - 50f)
        val centerPlusXPlusY = center.copy(x = center.x + stepX + 50f, y = center.y + stepX + 50f)


        modelVertices(centerPlusY, stepX, stepX, stepX)
        modelVertices(centerMinusY, stepX, stepX, stepX)
        modelVertices(centerMinusXMinusY, stepX, stepX, stepX)
        modelVertices(centerMinusXPlusY, stepX, stepX, stepX)
        modelVertices(centerPlusXMinusY, stepX, stepX, stepX)
        modelVertices(centerPlusXPlusY, stepX, stepX, stepX)

        val newPointBegin = Point3D(0f, 1f, 0f)
        val angleNewBegin = 90f / 100f

        rotate(angleNewBegin, newPointBegin, 0)
        rotate(angleNewBegin, newPointBegin, 1)
        rotate(angleNewBegin, newPointBegin, 2)
        rotate(angleNewBegin, newPointBegin, 3)
        rotate(angleNewBegin, newPointBegin, 4)

        val newPoint = Point3D(0f, 1f, 0f)
        val angleNew = 45f / 100f


        rotate(angleNew, newPoint, 5)
        rotate(angleNew, newPoint, 6)
        rotate(angleNew, newPoint, 7)
        rotate(angleNew, newPoint, 8)
        rotate(angleNew, newPoint, 9)
        rotate(angleNew, newPoint, 10)
        /*val list = listOf(0, 1, -1)

        for (x in list) {
            val xi = x * (stepX - x * 50f)
            for (y in list) {
                val yi = y * (stepX + y * 50f)
                for (z in list) {
                    val zi = z * (stepX + z * 50f)
                    val centerNew =
                        center.copy(x = center.x + xi, y = center.y + yi, z = center.z + zi)
                    modelVertices(centerNew, stepX, stepX, stepX)
                }
            }
        }*/

        Log.d("rubik", "size rubik = ${getSize()} **********************")

    }


    private fun modelVertices(centerNew: Point3D, edgeX: Float, edgeY: Float, edgeZ: Float) {
        vertices2 = mutableListOf(
            Point3D(
                centerNew.x - edgeX / 2,
                centerNew.y - edgeY / 2,
                centerNew.z - edgeZ / 2
            ), //1  - - -
            Point3D(
                centerNew.x + edgeX / 2,
                centerNew.y - edgeY / 2,
                centerNew.z - edgeZ / 2
            ), //2. + - -
            Point3D(
                centerNew.x + edgeX / 2,
                centerNew.y + edgeY / 2,
                centerNew.z - edgeZ / 2
            ), //3. + + -
            Point3D(
                centerNew.x - edgeX / 2,
                centerNew.y + edgeY / 2,
                centerNew.z - edgeZ / 2
            ), //4. - + -
            Point3D(
                centerNew.x - edgeX / 2,
                centerNew.y - edgeY / 2,
                centerNew.z + edgeZ / 2
            ), //5. - - +
            Point3D(
                centerNew.x + edgeX / 2,
                centerNew.y - edgeY / 2,
                centerNew.z + edgeZ / 2
            ), //6. + - +
            Point3D(
                centerNew.x + edgeX / 2,
                centerNew.y + edgeY / 2,
                centerNew.z + edgeZ / 2
            ), //7. + + +
            Point3D(
                centerNew.x - edgeX / 2,
                centerNew.y + edgeY / 2,
                centerNew.z + edgeZ / 2
            ), //8. - + +
        )
        verticesList.add(vertices2)

    }


    fun rotate(angle: Float, axis: Point3D, vertexNumber: Int) {
        // super.rotate(angle, axis)
        verticesList[vertexNumber].also { vertex ->

            val sin = sin(angle)
            val cos = cos(angle)
            vertex.forEach {
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
    }

    fun drawPath(path: androidx.compose.ui.graphics.Path, vert: Int) {
        // super.drawPath(path)
        verticesList[vert].also { vertex ->

            path.moveTo(vertex[0].x, vertex[0].y)
            for (i in 1..3) {
                path.lineTo(vertex[i].x, vertex[i].y)
            }
            path.lineTo(vertex[0].x, vertex[0].y)

            path.moveTo(vertex[4].x, vertex[4].y)
            for (i in 5..7) {
                path.lineTo(vertex[i].x, vertex[i].y)
            }
            path.lineTo(vertex[4].x, vertex[4].y)

            for (i in 0..3) {
                path.moveTo(vertex[i].x, vertex[i].y)
                path.lineTo(vertex[i + 4].x, vertex[i + 4].y)
            }
        }
    }

}