package ru.kartollika.yandexcup.canvas

import androidx.compose.foundation.background
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
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.collections.immutable.persistentListOf
import ru.kartollika.yandexcup.R
import ru.kartollika.yandexcup.canvas.compose.BottomControls
import ru.kartollika.yandexcup.canvas.compose.ColorItem
import ru.kartollika.yandexcup.canvas.compose.ColorsPicker
import ru.kartollika.yandexcup.canvas.compose.DrawingCanvas
import ru.kartollika.yandexcup.canvas.compose.TopControls
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
  val state: CanvasState by viewModel.stateOwner.state.collectAsState()
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

  Surface(
    modifier = modifier,
  ) {
    Box {
      Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        TopControls(
          canvasState = state,
          modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
            .padding(horizontal = 16.dp)
            .statusBarsPadding(),
          undoChange = remember { ::undoChange },
          redoChange = remember { ::redoChange },
          addFrame = remember { ::addFrame },
          deleteFrame = remember { ::deleteFrame },
        )

        val canvasBackground = ImageBitmap.imageResource(R.drawable.canvas)
        DrawingCanvas(
          paths = {
            state.currentFrame.paths
          },
          currentPath = {
            state.currentFrame.currentPath
          },
          previousPaths = {
            state.previousFrame?.paths
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

        BottomControls(
          canvasState = state,
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

      if (state.colorPickerVisible) {
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