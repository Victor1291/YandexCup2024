package ru.kartollika.yandexcup.canvas.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import ru.kartollika.yandexcup.R
import ru.kartollika.yandexcup.canvas.mvi.DrawMode.Erase
import ru.kartollika.yandexcup.canvas.mvi.DrawMode.Pencil
import ru.kartollika.yandexcup.canvas.mvi.EditorConfiguration

@Composable fun BottomControls(
  editorConfiguration: EditorConfiguration,
  modifier: Modifier = Modifier,
  onPencilClick: () -> Unit = {},
  onEraseClick: () -> Unit = {},
  onColorClick: () -> Unit = {},
  onBrushSizeClick: () -> Unit = {},
  onShapesClick: () -> Unit = {},
) {
  Controls(
    modifier = modifier,
    centerSpacedBy = 16.dp,
    startControls = {
      BrushSizeControl(
        editorConfiguration = editorConfiguration,
        onBrushSizeClick = onBrushSizeClick
      )
    },
    centerControls = {
      EditorButtons(
        onPencilClick = onPencilClick,
        editorConfiguration = editorConfiguration,
        onEraseClick = onEraseClick,
        onColorClick = onColorClick,
        onShapesClick = onShapesClick
      )
    },
  )
}

@Composable fun BrushSizeControl(
  editorConfiguration: EditorConfiguration,
  onBrushSizeClick: () -> Unit,
) {
  Box(
    contentAlignment = Alignment.Center
  ) {
    Spacer(
      modifier = Modifier
        .size(32.dp)
        .clip(CircleShape)
        .border(
          width = 2.dp,
          color = if (editorConfiguration.brushSizePickerVisible) {
            MaterialTheme.colorScheme.primary
          } else {
            LocalContentColor.current
          },
          shape = CircleShape
        )
        .clickable {
          onBrushSizeClick()
        }
    )

    Spacer(
      modifier = Modifier
        .size(editorConfiguration.brushSize.dp * 0.2f)
        .background(LocalContentColor.current, CircleShape)
    )
  }
}

@Composable
private fun EditorButtons(
  onPencilClick: () -> Unit,
  editorConfiguration: EditorConfiguration,
  onEraseClick: () -> Unit,
  onColorClick: () -> Unit,
  onShapesClick: () -> Unit = {},
) {
  ActionIcon(
    modifier = Modifier
      .size(32.dp),
    icon = R.drawable.pencil,
    onClick = onPencilClick,
    tint = if (editorConfiguration.currentMode == Pencil) {
      MaterialTheme.colorScheme.primary
    } else {
      MaterialTheme.colorScheme.onSurface
    }
  )

  ActionIcon(
    modifier = Modifier
      .size(32.dp),
    icon = R.drawable.erase,
    onClick = onEraseClick,
    tint = if (editorConfiguration.currentMode == Erase) {
      MaterialTheme.colorScheme.primary
    } else {
      MaterialTheme.colorScheme.onSurface
    }
  )

  ActionIcon(
    modifier = Modifier
      .size(32.dp),
    icon = R.drawable.instruments,
    onClick = onShapesClick,
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
      .background(editorConfiguration.color, CircleShape)
      .clickable {
        onColorClick()
      },
  )
}