package ru.kartollika.yandexcup.canvas.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.collections.immutable.persistentListOf
import ru.kartollika.yandexcup.R
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.DrawDrag
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.DrawFinish
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.DrawStart
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.EraseClick
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.PencilClick
import ru.kartollika.yandexcup.canvas.mvi.CanvasState
import ru.kartollika.yandexcup.canvas.vm.CanvasViewModel
import ru.kartollika.yandexcup.ui.theme.YandexCup2024Theme

@Composable
fun CanvasScreen(
  modifier: Modifier = Modifier,
  viewModel: CanvasViewModel = hiltViewModel()
) {
  val canvasState: CanvasState by viewModel.stateOwner.state.collectAsState()
  val actionConsumer = viewModel.actionConsumer

  fun onEraseClick() {
    actionConsumer.consumeAction(EraseClick)
  }

  fun onPencilClick() {
    actionConsumer.consumeAction(PencilClick)
  }

  fun changeColor() {
    actionConsumer.consumeAction(CanvasAction.ChangeColor)
  }

  fun undoChange() {
    actionConsumer.consumeAction(CanvasAction.UndoChange)
  }

  fun redoChange() {
    actionConsumer.consumeAction(CanvasAction.RedoChange)
  }

  fun onColorChanged(color: Color) {
    actionConsumer.consumeAction(CanvasAction.OnColorChanged(color))
  }

  fun addFrame() {
    actionConsumer.consumeAction(CanvasAction.AddNewFrame)
  }

  fun deleteFrame() {
    actionConsumer.consumeAction(CanvasAction.DeleteFrame)
  }

  fun startAnimation() {
    actionConsumer.consumeAction(CanvasAction.StartAnimation)
  }

  fun stopAnimation() {
    actionConsumer.consumeAction(CanvasAction.StopAnimation)
  }

  Surface(
    modifier = modifier,
  ) {
    Box {
      Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        TopControls(
          modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
            .padding(horizontal = 16.dp)
            .statusBarsPadding(),
          startControls = {
            if (canvasState.isPreviewAnimation) return@TopControls
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
          },
          centerControls = {
            if (canvasState.isPreviewAnimation) return@TopControls
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
          },
          endControls = {
            Icon(
              modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .alpha(if (canvasState.isPreviewAnimation) 1f else 0.3f)
                .clickable(
                  enabled = canvasState.isPreviewAnimation
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
                .alpha(if (canvasState.isPreviewAnimation) 0.3f else 1f)
                .clickable(
                  enabled = !canvasState.isPreviewAnimation
                ) {
                  startAnimation()
                },
              painter = painterResource(R.drawable.play),
              tint = Color.White,
              contentDescription = null
            )
          },
        )

        val canvasBackground = ImageBitmap.imageResource(R.drawable.canvas)
        DrawingCanvas(
          paths = {
            canvasState.currentFrame.paths
          },
          currentPath = {
            canvasState.currentFrame.currentPath
          },
          previousPaths = {
            if (canvasState.isPreviewAnimation) return@DrawingCanvas null
            canvasState.previousFrame?.paths
          },
          modifier = Modifier
            .weight(1f)
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(32.dp))
            .drawBehind {
              drawImage(canvasBackground)
            },
          onDragStart = {
            viewModel.actionConsumer.consumeAction(DrawStart(it))
          },
          onDrag = {
            viewModel.actionConsumer.consumeAction(DrawDrag(it))
          },
          onDragEnd = {
            viewModel.actionConsumer.consumeAction(DrawFinish)
          },
          onDragCancel = {
            viewModel.actionConsumer.consumeAction(DrawFinish)
          }
        )

        if (!canvasState.isPreviewAnimation) {
          BottomControls(
            canvasState = canvasState,
            modifier = Modifier
              .fillMaxWidth()
              .navigationBarsPadding()
              .padding(horizontal = 16.dp)
              .padding(bottom = 16.dp),
            onEraseClick = remember { ::onEraseClick },
            onPencilClick = remember { ::onPencilClick },
            onChangeColor = remember { ::changeColor },
          )
        }
      }

      if (canvasState.colorPickerVisible) {
        ColorsPicker(
          modifier = Modifier
            .padding(horizontal = 48.dp)
            .navigationBarsPadding()
            .padding(bottom = 64.dp)
            .align(Alignment.BottomCenter)
            .background(Color.Gray, RoundedCornerShape(4.dp))
            .padding(16.dp),
          smallPickerColors = persistentListOf(
            Color.White,
            Color.Red,
            Color.Blue,
            Color.Black,
          ),
          colorItem = { color ->
            ColorItem(
              color = color,
              modifier = Modifier
                .size(32.dp)
                .clip(CircleShape),
              onPick = {
                onColorChanged(color)
              }
            )
          },
        )
      }
    }
  }
}

@Preview
@Composable
private fun CanvasScreenPreview() {
  YandexCup2024Theme {
    CanvasScreen(
      modifier = Modifier.fillMaxSize(),
    )
  }
}