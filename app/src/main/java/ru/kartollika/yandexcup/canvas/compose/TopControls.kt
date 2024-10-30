package ru.kartollika.yandexcup.canvas.compose

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
  canUndo: Boolean = false,
  canRedo: Boolean = false,
  copyFrame: () -> Unit = {},
  showFrames: () -> Unit = {},
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
        copyFrame = copyFrame,
        showFrames = showFrames
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
  startAnimation: () -> Unit,
) {
  ActionIcon(
    modifier = Modifier
      .size(32.dp),
    icon = R.drawable.pause,
    onClick = stopAnimation,
    enabled = editorConfiguration.isPreviewAnimation,
  )

  ActionIcon(
    modifier = Modifier
      .size(32.dp),
    icon = R.drawable.play,
    onClick = startAnimation,
    enabled = !editorConfiguration.isPreviewAnimation,
  )
}

@Composable
private fun FramesButtons(
  editorConfiguration: EditorConfiguration,
  deleteFrame: () -> Unit,
  addFrame: () -> Unit,
  copyFrame: () -> Unit,
  showFrames: () -> Unit,
) {
  if (editorConfiguration.isPreviewAnimation) return
  ActionIcon(
    modifier = Modifier
      .size(32.dp),
    icon = R.drawable.bin,
    onClick = deleteFrame,
  )

  ActionIcon(
    modifier = Modifier
      .size(32.dp),
    icon = R.drawable.file_add,
    onClick = addFrame,
  )

  ActionIcon(
    modifier = Modifier
      .size(32.dp),
    icon = R.drawable.layers,
    onClick = showFrames,
  )

  ActionIcon(
    modifier = Modifier
      .size(32.dp),
    icon = R.drawable.file_plus,
    onClick = copyFrame,
  )
}

@Composable
private fun UndoRedoButtons(
  editorConfiguration: EditorConfiguration,
  canUndo: Boolean,
  undoChange: () -> Unit,
  canRedo: Boolean,
  redoChange: () -> Unit,
) {
  if (editorConfiguration.isPreviewAnimation) return
  ActionIcon(
    modifier = Modifier
      .size(32.dp),
    icon = R.drawable.undo,
    onClick = undoChange,
    enabled = canUndo,
  )

  ActionIcon(
    modifier = Modifier
      .size(32.dp),
    icon = R.drawable.redo,
    onClick = redoChange,
    enabled = canRedo,
  )
}