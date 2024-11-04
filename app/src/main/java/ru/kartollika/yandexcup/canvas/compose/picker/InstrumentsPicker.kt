package ru.kartollika.yandexcup.canvas.compose.picker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ru.kartollika.yandexcup.R
import ru.kartollika.yandexcup.canvas.Shape
import ru.kartollika.yandexcup.canvas.Shape.Arrow
import ru.kartollika.yandexcup.canvas.Shape.Circle
import ru.kartollika.yandexcup.canvas.Shape.Square
import ru.kartollika.yandexcup.canvas.Shape.Straight
import ru.kartollika.yandexcup.canvas.Shape.Triangle
import ru.kartollika.yandexcup.canvas.compose.ActionIcon

@Composable
fun InstrumentsPicker(
  modifier: Modifier = Modifier,
  selectShape: (Shape) -> Unit = {},
) {
  Picker(
    modifier = modifier,
  ) {
    Column {
      Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
      ) {
        ActionIcon(
          modifier = Modifier
            .size(32.dp),
          onClick = {
            selectShape(Square)
          },
          icon = R.drawable.square,
          tint = Color.White,
          contentDescription = "Квадрат"
        )

        ActionIcon(
          modifier = Modifier
            .size(32.dp),
          onClick = {
            selectShape(Circle)
          },
          icon = R.drawable.circle,
          tint = Color.White,
          contentDescription = "Круг"
        )

        ActionIcon(
          modifier = Modifier
            .size(32.dp),
          onClick = {
            selectShape(Triangle)
          },
          icon = R.drawable.triangle,
          tint = Color.White,
          contentDescription = "Треугольник"
        )

        ActionIcon(
          modifier = Modifier
            .size(32.dp),
          onClick = {
            selectShape(Arrow)
          },
          icon = R.drawable.arrow_up,
          tint = Color.White,
          contentDescription = "Стрелка"
        )

        ActionIcon(
          modifier = Modifier
            .size(32.dp)
            .rotate(-45f),
          onClick = {
            selectShape(Straight)
          },
          icon = R.drawable.straight,
          tint = Color.White,
          contentDescription = "Прямая линия"
        )
      }
    }
  }
}