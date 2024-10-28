package ru.kartollika.yandexcup.canvas.compose

import androidx.compose.foundation.clickable
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
import ru.kartollika.yandexcup.canvas.mvi.EditorConfiguration

@Composable fun TopControls(
  editorConfiguration: EditorConfiguration,
  modifier: Modifier = Modifier,
  undoChange: () -> Unit = {},
  redoChange: () -> Unit = {},
  deleteFrame: () -> Unit = {},
  addFrame: () -> Unit = {},
  stopAnimation: () -> Unit = {},
  startAnimation: () -> Unit = {},
  canUndo: () -> Boolean = { false },
  canRedo: () -> Boolean = { false },
  copyFrame: () -> Unit = {}
) {
  Controls(
    modifier = modifier,
    startControls = {
      UndoRedoButtons(
        editorConfiguration = editorConfiguration,
        canUndo = canUndo,
        undoChange = undoChange,
        canRedo = canRedo,
        redoChange = redoChange
      )
    },
    centerControls = {
      FramesButtons(
        editorConfiguration = editorConfiguration,
        deleteFrame = deleteFrame,
        addFrame = addFrame,
        copyFrame = copyFrame
      )
    },
    endControls = {
      AnimationButtons(
        editorConfiguration = editorConfiguration,
        stopAnimation = stopAnimation,
        startAnimation = startAnimation
      )
    },
  )
}

@Composable
private fun AnimationButtons(
  editorConfiguration: EditorConfiguration,
  stopAnimation: () -> Unit,
  startAnimation: () -> Unit
) {
  Icon(
    modifier = Modifier
      .size(32.dp)
      .clip(CircleShape)
      .alpha(if (editorConfiguration.isPreviewAnimation) 1f else 0.3f)
      .clickable(
        enabled = editorConfiguration.isPreviewAnimation
      ) {
        stopAnimation()
      },
    painter = painterResource(R.drawable.pause),
    tint = Color.White,
    contentDescription = null
  )

  Icon(
    modifier = Modifier
      .size(32.dp)
      .clip(CircleShape)
      .alpha(if (editorConfiguration.isPreviewAnimation) 0.3f else 1f)
      .clickable(
        enabled = !editorConfiguration.isPreviewAnimation
      ) {
        startAnimation()
      },
    painter = painterResource(R.drawable.play),
    tint = Color.White,
    contentDescription = null
  )
}

@Composable
private fun FramesButtons(
  editorConfiguration: EditorConfiguration,
  deleteFrame: () -> Unit,
  addFrame: () -> Unit,
  copyFrame: () -> Unit
) {
  if (editorConfiguration.isPreviewAnimation) return
  Icon(
    modifier = Modifier
      .size(32.dp)
      .clip(CircleShape)
      .clickable { deleteFrame() },
    painter = painterResource(R.drawable.bin),
    tint = Color.White,
    contentDescription = null
  )

  Icon(
    modifier = Modifier
      .size(32.dp)
      .clip(CircleShape)
      .clickable { addFrame() },
    painter = painterResource(R.drawable.file_add),
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

  Icon(
    modifier = Modifier
      .size(32.dp)
      .clip(CircleShape)
      .clickable { copyFrame() },
    painter = painterResource(R.drawable.file_plus),
    tint = Color.White,
    contentDescription = null
  )
}

@Composable
private fun UndoRedoButtons(
  editorConfiguration: EditorConfiguration,
  canUndo: () -> Boolean,
  undoChange: () -> Unit,
  canRedo: () -> Boolean,
  redoChange: () -> Unit
) {
  if (editorConfiguration.isPreviewAnimation) return
  Icon(
    modifier = Modifier
      .size(32.dp)
      .clip(CircleShape)
      .clickable(
        enabled = canUndo()
      ) {
        undoChange()
      }
      .graphicsLayer {
        alpha = if (canUndo()) 1f else 0.3f
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
        enabled = canRedo()
      ) {
        redoChange()
      }
      .graphicsLayer {
        alpha = if (canRedo()) 1f else 0.3f
      },
    painter = painterResource(R.drawable.redo),
    tint = Color.White,
    contentDescription = null
  )
}