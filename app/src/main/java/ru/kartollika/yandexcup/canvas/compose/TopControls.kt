package ru.kartollika.yandexcup.canvas.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ru.kartollika.yandexcup.R
import ru.kartollika.yandexcup.canvas.mvi.CanvasState

@Composable
fun TopControls(
  canvasState: CanvasState,
  modifier: Modifier = Modifier,
  undoChange: () -> Unit = {},
  redoChange: () -> Unit = {},
) {
  Row(
    modifier = modifier,
    horizontalArrangement = Arrangement.SpaceBetween,
  ) {
    Row(
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      Icon(
        modifier = Modifier
          .size(32.dp)
          .clip(CircleShape)
          .clickable(
            enabled = canvasState.canUndo
          ) {
            undoChange()
          }
          .graphicsLayer {
            alpha = if (canvasState.canUndo) 1f else 0.3f
          },
        painter = painterResource(R.drawable.undo),
        tint = Color.White,
        contentDescription = null,
      )

      Icon(
        modifier = Modifier
          .size(32.dp)
          .clip(CircleShape)
          .clickable(
            enabled = canvasState.canRedo
          ) {
            redoChange()
          }
          .graphicsLayer {
            alpha = if (canvasState.canRedo) 1f else 0.3f
          },
        painter = painterResource(R.drawable.redo),
        tint = Color.White,
        contentDescription = null
      )
    }

    Row(
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      Icon(
        modifier = Modifier
          .size(32.dp)
          .alpha(0.3f),
        painter = painterResource(R.drawable.bin),
        tint = Color.White,
        contentDescription = null
      )

      Icon(
        modifier = Modifier
          .size(32.dp)
          .alpha(0.3f),
        painter = painterResource(R.drawable.file_plus),
        tint = Color.White,
        contentDescription = null
      )

      Icon(
        modifier = Modifier
          .size(32.dp)
          .alpha(0.3f),
        painter = painterResource(R.drawable.layers),
        tint = Color.White,
        contentDescription = null
      )
    }

    Row(
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      Icon(
        modifier = Modifier
          .size(32.dp)
          .alpha(0.3f),
        painter = painterResource(R.drawable.pause),
        tint = Color.White,
        contentDescription = null
      )

      Icon(
        modifier = Modifier
          .size(32.dp)
          .alpha(0.3f),
        painter = painterResource(R.drawable.play),
        tint = Color.White,
        contentDescription = null
      )
    }
  }
}