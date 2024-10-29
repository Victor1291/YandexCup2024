package ru.kartollika.yandexcup.canvas.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ru.kartollika.yandexcup.R
import ru.kartollika.yandexcup.canvas.Shape
import ru.kartollika.yandexcup.canvas.Shape.Circle
import ru.kartollika.yandexcup.canvas.Shape.Square
import ru.kartollika.yandexcup.canvas.Shape.Triangle

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
        Icon(
          modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .padding(2.dp)
            .clickable { selectShape(Square) },
          painter = painterResource(R.drawable.square),
          contentDescription = null,
          tint = Color.White,
        )

        Icon(
          modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .padding(2.dp)
            .clickable { selectShape(Circle) },
          painter = painterResource(R.drawable.circle),
          contentDescription = null,
          tint = Color.White,
        )

        Icon(
          modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .padding(2.dp)
            .clickable { selectShape(Triangle) },
          painter = painterResource(R.drawable.triangle),
          contentDescription = null,
          tint = Color.White,
        )

        /*Icon(
          modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .clickable { selectShape(Arrow) },
          painter = painterResource(R.drawable.arrow_up),
          contentDescription = null,
          tint = Color.White,
        )*/
      }
    }
  }
}