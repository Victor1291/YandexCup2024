package ru.kartollika.yandexcup.canvas.sources

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Path
import ru.kartollika.yandexcup.canvas.Shape
import ru.kartollika.yandexcup.canvas.Shape.Arrow
import ru.kartollika.yandexcup.canvas.Shape.Circle
import ru.kartollika.yandexcup.canvas.Shape.Square
import ru.kartollika.yandexcup.canvas.Shape.Straight
import ru.kartollika.yandexcup.canvas.Shape.Triangle
import ru.kartollika.yandexcup.canvas.Shape.Cubik
import javax.inject.Inject

class ShapeDrawer @Inject constructor() {
  fun drawShape(shape: Shape, angle: Float): Path {
    val path = Path()
    when (shape) {
      Circle -> drawOval(path)
      Square -> drawRect(path)
      Triangle -> drawTriangle(path)
      Arrow -> drawArrow(path)
      Straight -> drawStraight(path)
      Cubik -> drawCubik(path,angle)
    }
    return path
  }

  private fun drawStraight(path: Path) {
    path.moveTo(100f, 100f)
    path.lineTo(400f, 100f)
  }

  private fun drawArrow(path: Path) {
    path.moveTo(100f, 100f)
    path.lineTo(50f, 150f)
    path.moveTo(100f, 100f)
    path.lineTo(150f, 150f)
    path.moveTo(100f, 100f)
    path.lineTo(100f, 250f)
  }

  private fun drawRect(path: Path) {
    path.addRect(Rect(100f, 100f, 400f, 400f))
  }

  private fun drawOval(path: Path) {
    path.addOval(Rect(100f, 100f, 400f, 400f))
  }

  private fun drawTriangle(path: Path) {
    path.moveTo(100f, 100f)
    path.lineTo(200f, 250f)
    path.lineTo(0f, 250f)
    path.lineTo(100f, 100f)
  }

  private fun drawCubik(path: Path, angle: Float) {
    val cube = Cube(Point3D(200f, 200f, 200f), 200f)
    cube.rotate(angle / 100, Point3D(1f, 0f, 0f))
    cube.rotate(-angle / 100, Point3D(0f, 1f, 0f))
    cube.drawPath(path)
  }

}