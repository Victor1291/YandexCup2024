package ru.kartollika.yandexcup.canvas.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ru.kartollika.yandexcup.R
import ru.kartollika.yandexcup.canvas.mvi.CanvasState
import kotlin.random.Random

@Composable fun BottomControls(
  canvasState: CanvasState,
  modifier: Modifier = Modifier,
  onEraseClick: () -> Unit = {},
  onPencilClick: () -> Unit = {},
  onChangeColor: (Color) -> Unit = {},
) {
  Row(
    modifier = modifier,
    horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
  ) {
    Icon(
      modifier = Modifier.size(32.dp),
      painter = painterResource(R.drawable.pencil),
      tint = Color.White,
      contentDescription = null,
    )

    Icon(
      modifier = Modifier.size(32.dp),
      painter = painterResource(R.drawable.brush),
      contentDescription = null,
      tint = Color.White
    )

    Icon(
      modifier = Modifier.size(32.dp),
      painter = painterResource(R.drawable.erase),
      contentDescription = null,
      tint = Color.White
    )

    Icon(
      modifier = Modifier.size(32.dp),
      painter = painterResource(R.drawable.instruments),
      contentDescription = null,
      tint = Color.White
    )

    Spacer(
      modifier = Modifier
        .size(32.dp)
        .background(canvasState.color, CircleShape)
        .clickable {
          onChangeColor(
            if (Random.nextBoolean()) Color.Red else Color.Blue
          )
        },
    )
  }
}