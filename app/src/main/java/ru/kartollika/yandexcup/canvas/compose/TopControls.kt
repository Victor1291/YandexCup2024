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
    icon = R.drawable.pause,
    onClick = stopAnimation,
    modifier = Modifier
      .size(32.dp),
    enabled = editorConfiguration.isPreviewAnimation,
  )

  ActionIcon(
    icon = R.drawable.play,
    onClick = startAnimation,
    modifier = Modifier
      .size(32.dp),
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
    icon = R.drawable.bin,
    onClick = deleteFrame,
    modifier = Modifier
      .size(32.dp),
  )

  ActionIcon(
    icon = R.drawable.file_add,
    onClick = addFrame,
    modifier = Modifier
      .size(32.dp),
  )

  ActionIcon(
    icon = R.drawable.layers,
    onClick = showFrames,
    modifier = Modifier
      .size(32.dp),
  )

  ActionIcon(
    icon = R.drawable.file_plus,
    onClick = copyFrame,
    modifier = Modifier
      .size(32.dp),
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
    icon = R.drawable.undo,
    onClick = undoChange,
    modifier = Modifier
      .size(32.dp),
    enabled = canUndo,
  )

  ActionIcon(
    icon = R.drawable.redo,
    onClick = redoChange,
    modifier = Modifier
      .size(32.dp),
    enabled = canRedo,
  )
}