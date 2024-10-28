package ru.kartollika.yandexcup.canvas.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ru.kartollika.yandexcup.R
import ru.kartollika.yandexcup.canvas.mvi.CanvasState
import ru.kartollika.yandexcup.canvas.mvi.DrawMode.Erase
import ru.kartollika.yandexcup.canvas.mvi.DrawMode.Pencil

@Composable fun BottomControls(
  canvasState: CanvasState,
  modifier: Modifier = Modifier,
  onEraseClick: () -> Unit = {},
  onPencilClick: () -> Unit = {},
  onChangeColor: () -> Unit = {},
) {
  Row(
    modifier = modifier,
    horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
  ) {
    Icon(
      modifier = Modifier
        .size(32.dp)
        .clip(CircleShape)
        .clickable {
          onPencilClick()
        },
      painter = painterResource(R.drawable.pencil),
      tint = if (canvasState.currentMode == Pencil) {
        MaterialTheme.colorScheme.primary
      } else {
        Color.White
      },
      contentDescription = null,
    )

    Icon(
      modifier = Modifier
        .size(32.dp)
        .clip(CircleShape)
        .clickable { onEraseClick() },
      painter = painterResource(R.drawable.erase),
      contentDescription = null,
      tint = if (canvasState.currentMode == Erase) {
        MaterialTheme.colorScheme.primary
      } else {
        Color.White
      },
    )

    Icon(
      modifier = Modifier
        .size(32.dp)
        .alpha(0.3f),
      painter = painterResource(R.drawable.instruments),
      contentDescription = null,
      tint = Color.White
    )

    Spacer(
      modifier = Modifier
        .size(32.dp)
        .border(
          width = 1.5.dp,
          color = MaterialTheme.colorScheme.primary,
          shape = CircleShape
        )
        .padding(4.dp)
        .background(canvasState.color, CircleShape)
        .clickable {
          onChangeColor()
        },
    )
  }
}