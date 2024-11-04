package ru.kartollika.yandexcup.canvas.compose.controls

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.layout
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import ru.kartollika.yandexcup.R
import ru.kartollika.yandexcup.canvas.compose.ActionIcon
import ru.kartollika.yandexcup.canvas.mvi.DrawMode.Erase
import ru.kartollika.yandexcup.canvas.mvi.DrawMode.Pencil
import ru.kartollika.yandexcup.canvas.mvi.DrawMode.Transform
import ru.kartollika.yandexcup.canvas.mvi.EditorConfiguration

@Composable fun BottomControls(
  editorConfiguration: EditorConfiguration,
  modifier: Modifier = Modifier,
  onPencilClick: () -> Unit = {},
  onEraseClick: () -> Unit = {},
  onColorClick: () -> Unit = {},
  onBrushSizeClick: () -> Unit = {},
  onShapesClick: () -> Unit = {},
  onTransformModeClick: () -> Unit = {},
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
    endControls = {
      ActionIcon(
        R.drawable.hand,
        onClick = onTransformModeClick,
        tint = if (editorConfiguration.currentMode == Transform) {
          MaterialTheme.colorScheme.primary
        } else {
          MaterialTheme.colorScheme.onSurface
        },
        contentDescription = "Режим трансформации"
      )
    }
  )
}

@Composable fun BrushSizeControl(
  editorConfiguration: EditorConfiguration,
  onBrushSizeClick: () -> Unit,
) {
  Box(
    modifier = Modifier
      .semantics {
        contentDescription = "Размер кисти"
      },
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

    val brushSize by animateDpAsState(
      targetValue = editorConfiguration.brushSizeByMode.dp * 0.2f,
      animationSpec = tween(100)
    )

    Spacer(
      modifier = Modifier
        .layout { measurable: Measurable, constraints: Constraints ->
          val sizePx = brushSize.roundToPx()
          val placeable = measurable.measure(
            constraints.copy(
              minWidth = sizePx,
              maxWidth = sizePx,
              minHeight = sizePx,
              maxHeight = sizePx
            )
          )

          layout(sizePx, sizePx) {
            placeable.placeRelative(0, 0)
          }
        }
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
    icon = R.drawable.pencil,
    onClick = onPencilClick,
    modifier = Modifier
      .size(32.dp),
    tint = if (editorConfiguration.currentMode == Pencil) {
      MaterialTheme.colorScheme.primary
    } else {
      MaterialTheme.colorScheme.onSurface
    },
    contentDescription = "Режим рисования"
  )

  ActionIcon(
    icon = R.drawable.erase,
    onClick = onEraseClick,
    modifier = Modifier
      .size(32.dp),
    tint = if (editorConfiguration.currentMode == Erase) {
      MaterialTheme.colorScheme.primary
    } else {
      MaterialTheme.colorScheme.onSurface
    },
    contentDescription = "Режим стирания"
  )

  ActionIcon(
    icon = R.drawable.instruments,
    onClick = onShapesClick,
    modifier = Modifier
      .size(32.dp),
    contentDescription = "Вставить фигуру"
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
      .clip(CircleShape)
      .clickable {
        onColorClick()
      }
      .semantics {
        contentDescription = "Выбрать цвет"
      },
  )
}