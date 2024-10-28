package ru.kartollika.yandexcup.canvas.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
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
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.AnimationDelayChange
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.CopyFrame
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.DrawDrag
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.DrawFinish
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.DrawStart
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.EraseClick
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.PencilClick
import ru.kartollika.yandexcup.canvas.mvi.CanvasState
import ru.kartollika.yandexcup.canvas.mvi.DrawMode.Erase
import ru.kartollika.yandexcup.canvas.mvi.DrawMode.Pencil
import ru.kartollika.yandexcup.canvas.mvi.EditorConfiguration
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

  fun onDragStart(offset: Offset) {
    viewModel.actionConsumer.consumeAction(DrawStart(offset))
  }

  fun onDrag(offset: Offset) {
    viewModel.actionConsumer.consumeAction(DrawDrag(offset))
  }

  fun onDragEnd() {
    viewModel.actionConsumer.consumeAction(DrawFinish)
  }

  fun onDelayChanged(animationDelay: Float) {
    viewModel.actionConsumer.consumeAction(AnimationDelayChange(animationDelay))
  }

  fun copyFrame() {
    viewModel.actionConsumer.consumeAction(CopyFrame)
  }

  CanvasScreen(
    modifier = modifier,
    canvasState = canvasState,
    undoChange = remember { ::undoChange },
    redoChange = remember { ::redoChange },
    deleteFrame = remember { ::deleteFrame },
    addFrame = remember { ::addFrame },
    stopAnimation = remember { ::stopAnimation },
    startAnimation = remember { ::startAnimation },
    onDragStart = remember { ::onDragStart },
    onDrag = remember { ::onDrag },
    onDragEnd = remember { ::onDragEnd },
    onPencilClick = remember { ::onPencilClick },
    onEraseClick = remember { ::onEraseClick },
    onColorClick = remember { ::changeColor },
    onColorChanged = remember { ::onColorChanged },
    onDelayChanged = remember { ::onDelayChanged },
    copyFrame = remember { ::copyFrame }
  )
}

@Composable
private fun CanvasScreen(
  modifier: Modifier,
  canvasState: CanvasState,
  undoChange: () -> Unit = {},
  redoChange: () -> Unit = {},
  deleteFrame: () -> Unit = {},
  addFrame: () -> Unit = {},
  stopAnimation: () -> Unit = {},
  startAnimation: () -> Unit = {},
  onDragStart: (Offset) -> Unit = {},
  onDrag: (Offset) -> Unit = {},
  onDragEnd: () -> Unit = {},
  onPencilClick: () -> Unit = {},
  onEraseClick: () -> Unit = {},
  onColorClick: () -> Unit = {},
  onColorChanged: (Color) -> Unit = {},
  onDelayChanged: (Float) -> Unit = {},
  copyFrame: () -> Unit = {},
) {
  Surface(
    modifier = modifier,
  ) {
    Box {
      Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        TopControls(
          editorConfiguration = canvasState.editorConfiguration,
          modifier = Modifier
            .statusBarsPadding()
            .fillMaxWidth()
            .padding(top = 16.dp)
            .padding(horizontal = 16.dp),
          undoChange = undoChange,
          redoChange = redoChange,
          deleteFrame = deleteFrame,
          addFrame = addFrame,
          stopAnimation = stopAnimation,
          startAnimation = startAnimation,
          copyFrame = copyFrame,
        )

        val canvasBackground = ImageBitmap.imageResource(R.drawable.canvas)
        Canvas(
          modifier = modifier
            .weight(1f)
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(32.dp))
            .drawBehind {
              drawImage(canvasBackground)
            },
          canvasState = { canvasState },
          onDragStart = onDragStart,
          onDrag = onDrag,
          onDragEnd = onDragEnd,
        )

        if (canvasState.editorConfiguration.isPreviewAnimation) {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .navigationBarsPadding()
              .padding(horizontal = 16.dp)
              .padding(bottom = 16.dp)
          ) {
            Slider(
              value = canvasState.editorConfiguration.animationDelay.toFloat(),
              valueRange = 10f..1000f,
              onValueChange = { animationDelay ->
                onDelayChanged(animationDelay)
              },
            )
          }
        } else {
          BottomControls(
            modifier = Modifier
              .fillMaxWidth()
              .navigationBarsPadding()
              .padding(horizontal = 16.dp)
              .padding(bottom = 16.dp),
            editorConfiguration = canvasState.editorConfiguration,
            onPencilClick = onPencilClick,
            onEraseClick = onEraseClick,
            onColorClick = onColorClick
          )
        }
      }

      if (canvasState.editorConfiguration.colorPickerVisible) {
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

@Composable
private fun Canvas(
  canvasState: () -> CanvasState,
  modifier: Modifier = Modifier,
  onDragStart: (Offset) -> Unit = {},
  onDrag: (Offset) -> Unit = {},
  onDragEnd: () -> Unit = {},
) {
  DrawingCanvas(
    paths = {
      canvasState().currentFrame.paths
    },
    currentPath = {
      canvasState().currentFrame.currentPath
    },
    previousPaths = {
      if (canvasState().editorConfiguration.isPreviewAnimation) return@DrawingCanvas null
      canvasState().previousFrame?.paths
    },
    modifier = modifier,
    onDragStart = onDragStart,
    onDrag = onDrag,
    onDragEnd = onDragEnd,
    onDragCancel = onDragEnd
  )
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