package ru.kartollika.yandexcup.canvas.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize.Min
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.collections.immutable.persistentListOf
import ru.kartollika.yandexcup.R
import ru.kartollika.yandexcup.canvas.FrameIndex
import ru.kartollika.yandexcup.canvas.frames.FramesScreen
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.AnimationDelayChange
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.CopyFrame
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.DrawDrag
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.DrawFinish
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.DrawStart
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.EraseClick
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.HideFrames
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.PencilClick
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.ShowFrames
import ru.kartollika.yandexcup.canvas.mvi.CanvasState
import ru.kartollika.yandexcup.canvas.vm.CanvasViewModel
import ru.kartollika.yandexcup.ui.theme.YandexCup2024Theme
import ru.kartollika.yandexcup.components.Slider as YandexcupComponentsSlider

@OptIn(ExperimentalMaterial3Api::class)
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
    actionConsumer.consumeAction(CanvasAction.OnColorClick)
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

  fun onFastColorClicked(color: Color) {
    actionConsumer.consumeAction(CanvasAction.OnColorItemClicked(color))
  }

  fun addFrame() {
    actionConsumer.consumeAction(CanvasAction.AddNewFrame)
  }

  fun deleteFrame(frameIndex: FrameIndex) {
    actionConsumer.consumeAction(CanvasAction.DeleteFrame(frameIndex))
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

  fun showFrames() {
    viewModel.actionConsumer.consumeAction(ShowFrames)
  }

  fun hideFrames() {
    viewModel.actionConsumer.consumeAction(HideFrames)
  }

  fun selectFrame(frameIndex: Int) {
    viewModel.actionConsumer.consumeAction(CanvasAction.SelectFrame(frameIndex))
  }

  fun deleteAllFrames() {
    viewModel.actionConsumer.consumeAction(CanvasAction.DeleteAllFrames)
  }

  fun onBrushSizeClick() {
    viewModel.actionConsumer.consumeAction(CanvasAction.ShowBrushSizePicker)
  }

  fun changeBrushSize(size: Float) {
    viewModel.actionConsumer.consumeAction(CanvasAction.ChangeBrushSize(size))
  }

  fun onCustomColorClick() {
    viewModel.actionConsumer.consumeAction(CanvasAction.CustomColorClick)
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
    onFastColorClicked = remember { ::onFastColorClicked },
    onDelayChanged = remember { ::onDelayChanged },
    copyFrame = remember { ::copyFrame },
    showFrames = remember { ::showFrames },
    hideFrames = remember { ::hideFrames },
    selectFrame = remember { ::selectFrame },
    deleteAllFrames = remember { ::deleteAllFrames },
    onBrushSizeClick = remember { ::onBrushSizeClick },
    changeBrushSize = remember { ::changeBrushSize },
    onCustomColorClick = remember { ::onCustomColorClick }
  )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CanvasScreen(
  modifier: Modifier,
  canvasState: CanvasState,
  undoChange: () -> Unit = {},
  redoChange: () -> Unit = {},
  deleteFrame: (FrameIndex) -> Unit = {},
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
  onFastColorClicked: (Color) -> Unit = {},
  onDelayChanged: (Float) -> Unit = {},
  copyFrame: () -> Unit = {},
  showFrames: () -> Unit = {},
  hideFrames: () -> Unit = {},
  selectFrame: (Int) -> Unit = {},
  deleteAllFrames: () -> Unit = {},
  onBrushSizeClick: () -> Unit = {},
  changeBrushSize: (Float) -> Unit = {},
  onCustomColorClick: () -> Unit = {},
) {
  Surface(
    modifier = modifier,
  ) {

    if (canvasState.framesSheetVisible) {
      ModalBottomSheet(
        sheetState = rememberModalBottomSheetState(),
        onDismissRequest = {
          hideFrames()
        }
      ) {
        FramesScreen(
          modifier = Modifier.fillMaxWidth(),
          frames = canvasState.frames,
          selectFrame = selectFrame,
          deleteFrame = { index ->
            deleteFrame(FrameIndex.Index(index))
          },
          deleteAllFrames = deleteAllFrames
        )
      }
    }
  }

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
        deleteFrame = {
          deleteFrame(FrameIndex.Current)
        },
        addFrame = addFrame,
        stopAnimation = stopAnimation,
        startAnimation = startAnimation,
        copyFrame = copyFrame,
        canUndo = { canvasState.canUndo },
        canRedo = { canvasState.canRedo },
        showFrames = showFrames
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

      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(80.dp)
          .padding(bottom = 16.dp)
          .padding(horizontal = 16.dp)
          .navigationBarsPadding()
      ) {
        if (canvasState.editorConfiguration.isPreviewAnimation) {
          Row(
            modifier = Modifier
              .matchParentSize()
              .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
          ) {
            YandexcupComponentsSlider(
              modifier = Modifier
                .weight(1f)
                .height(24.dp),
              value = canvasState.editorConfiguration.animationDelay.toFloat(),
              valueRange = 10f..1000f,
              onValueChange = { animationDelay ->
                onDelayChanged(animationDelay)
              },
            )

            IconButton(
              modifier = Modifier,
              onClick = {}
            ) {
              Icon(
                imageVector = Icons.Default.Share,
                contentDescription = null,
                tint = Color.White
              )
            }
          }
        } else {
          BottomControls(
            modifier = Modifier
              .fillMaxSize()
              .padding(horizontal = 8.dp),
            editorConfiguration = canvasState.editorConfiguration,
            onPencilClick = onPencilClick,
            onEraseClick = onEraseClick,
            onColorClick = onColorClick,
            onBrushSizeClick = onBrushSizeClick
          )
        }
      }
    }

    if (canvasState.editorConfiguration.colorPickerVisible) {
      ColorsPicker(
        editorConfiguration = canvasState.editorConfiguration,
        modifier = Modifier
          .navigationBarsPadding()
          .width(Min)
          .padding(bottom = 64.dp)
          .align(Alignment.BottomCenter)
          .padding(16.dp),
        smallPickerColors = persistentListOf(
          Color.White,
          Color.Red,
          Color.Blue,
          Color.Black,
        ),
        customColorItem = {
          Icon(
            modifier = Modifier
              .size(32.dp)
              .clip(CircleShape)
              .clickable { onCustomColorClick() },
            painter = painterResource(R.drawable.palette),
            contentDescription = null,
            tint = Color.White
          )
        },
        fastColorClicked = onFastColorClicked,
        pickColor = onColorChanged
      )
    }

    if (canvasState.editorConfiguration.brushSizePickerVisible) {
      BrushSizePicker(
        modifier = Modifier
          .padding(bottom = 100.dp)
          .navigationBarsPadding()
          .align(Alignment.BottomCenter)
          .background(Color.Gray, RoundedCornerShape(4.dp))
          .padding(horizontal = 16.dp)
          .height(50.dp)
          .width(200.dp),
        editorConfiguration = canvasState.editorConfiguration,
        changeBrushSize = changeBrushSize
      )
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