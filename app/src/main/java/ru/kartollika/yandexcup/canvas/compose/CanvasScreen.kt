package ru.kartollika.yandexcup.canvas.compose

//import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.DrawDrag
//import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.DrawFinish
//import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.DrawStart
import android.content.Context
import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize.Min
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.collections.immutable.persistentListOf
import ru.kartollika.yandexcup.R
import ru.kartollika.yandexcup.canvas.CanvasMode
import ru.kartollika.yandexcup.canvas.CanvasMode.Draw
import ru.kartollika.yandexcup.canvas.FrameIndex
import ru.kartollika.yandexcup.canvas.FrameIndex.Current
import ru.kartollika.yandexcup.canvas.FrameIndex.Index
import ru.kartollika.yandexcup.canvas.Shape
import ru.kartollika.yandexcup.canvas.compose.controls.TopControls
import ru.kartollika.yandexcup.canvas.compose.frames.FramesScreen
import ru.kartollika.yandexcup.canvas.compose.picker.BrushSizePicker
import ru.kartollika.yandexcup.canvas.compose.picker.ColorsPicker
import ru.kartollika.yandexcup.canvas.compose.picker.InstrumentsPicker
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.AnimationDelayChange
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.CopyFrame
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.DrawFinish
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.EraseClick
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.HideFrames
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.PencilClick
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.ShowFrames
import ru.kartollika.yandexcup.canvas.mvi.CanvasEvent.ShareGif
import ru.kartollika.yandexcup.canvas.mvi.CanvasState
import ru.kartollika.yandexcup.canvas.mvi.DrawMode.Erase
import ru.kartollika.yandexcup.canvas.mvi.DrawMode.Pencil
import ru.kartollika.yandexcup.canvas.mvi.DrawMode.Transform
import ru.kartollika.yandexcup.canvas.mvi.PathWithProperties
import ru.kartollika.yandexcup.canvas.rememberCanvasDrawState
import ru.kartollika.yandexcup.canvas.vm.CanvasViewModel
import ru.kartollika.yandexcup.core.noIndicationClickable
import ru.kartollika.yandexcup.ui.theme.YandexCup2024Theme
import java.io.File
import kotlin.math.cos
import kotlin.math.sin
import ru.kartollika.yandexcup.components.Slider as YandexcupComponentsSlider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CanvasScreen(
  modifier: Modifier = Modifier,
  viewModel: CanvasViewModel = hiltViewModel(),
) {
  val canvasState: CanvasState by viewModel.stateOwner.state.collectAsState()
  val actionConsumer = viewModel.actionConsumer
  val eventConsumer = viewModel.eventsOwner

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

  fun onDragStart() {
    viewModel.actionConsumer.consumeAction(CanvasAction.DrawStart)
  }

  fun onDragEnd(pathWithProperties: PathWithProperties) {
    viewModel.actionConsumer.consumeAction(DrawFinish(pathWithProperties))
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

  fun onShapeClick() {
    viewModel.actionConsumer.consumeAction(CanvasAction.OpenShapes)
  }

  fun selectShape(shape: Shape) {
    viewModel.actionConsumer.consumeAction(CanvasAction.SelectShape(shape))
  }

  fun exportToGif() {
    viewModel.actionConsumer.consumeAction(CanvasAction.ExportToGif)
  }

  fun confirmGenerateDummyFrames(framesCount: Int) {
    viewModel.actionConsumer.consumeAction(CanvasAction.GenerateDummyFrames(framesCount))
  }

  fun onTransformModeClick() {
    viewModel.actionConsumer.consumeAction(CanvasAction.TransformModeClick)
  }

  fun onCanvasSizeChanged(canvasSize: IntSize) {
    viewModel.actionConsumer.consumeAction(CanvasAction.CanvasMeasured(canvasSize))
  }

  val context = LocalContext.current
  LaunchedEffect(eventConsumer) {
    eventConsumer.events.collect { event ->
      when (event) {
        is ShareGif -> shareGif(context, event.file)
      }
    }
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
    onCustomColorClick = remember { ::onCustomColorClick },
    onShapeClick = remember { ::onShapeClick },
    selectShape = remember { ::selectShape },
    exportToGif = remember { ::exportToGif },
    confirmGenerateDummyFrames = remember { ::confirmGenerateDummyFrames },
    onTransformModeClick = remember { ::onTransformModeClick },
    onCanvasSizeChanged = remember { ::onCanvasSizeChanged }
  )
}

fun shareGif(context: Context, file: File) {
  val intentShareFile = Intent(Intent.ACTION_SEND)
  if (file.exists()) {
    intentShareFile.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    intentShareFile.setType("file/*")
    intentShareFile.putExtra(
      Intent.EXTRA_STREAM,
      FileProvider.getUriForFile(
        context,
        context.applicationContext.packageName + ".provider",
        file
      )
    )

    intentShareFile.putExtra(
      Intent.EXTRA_SUBJECT,
      "Sharing File..."
    )
    intentShareFile.putExtra(Intent.EXTRA_TEXT, "Sharing File...")

    context.startActivity(Intent.createChooser(intentShareFile, "Share File"))
  }
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
  onDragStart: () -> Unit = {},
  onDragEnd: (PathWithProperties) -> Unit = {},
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
  onShapeClick: () -> Unit = {},
  selectShape: (Shape) -> Unit = {},
  exportToGif: () -> Unit = {},
  confirmGenerateDummyFrames: (Int) -> Unit = {},
  onTransformModeClick: () -> Unit = {},
  onCanvasSizeChanged: (IntSize) -> Unit = {},
) {
  BackHandlers(
    canvasState = canvasState,
    stopAnimation = stopAnimation,
    onColorClick = onColorClick,
    onBrushSizeClick = onBrushSizeClick,
    onShapeClick = onShapeClick
  )

  Surface(
    modifier = modifier,
  ) {
    Box {
      Content(
        canvasState = canvasState,
        undoChange = undoChange,
        redoChange = redoChange,
        deleteFrame = deleteFrame,
        addFrame = addFrame,
        stopAnimation = stopAnimation,
        startAnimation = startAnimation,
        copyFrame = copyFrame,
        showFrames = showFrames,
        modifier = modifier,
        onDragStart = onDragStart,
        onDragEnd = onDragEnd,
        onDelayChanged = onDelayChanged,
        onPencilClick = onPencilClick,
        onEraseClick = onEraseClick,
        onColorClick = onColorClick,
        onBrushSizeClick = onBrushSizeClick,
        onShapeClick = onShapeClick,
        exportToGif = exportToGif,
        onTransformModeClick = onTransformModeClick,
        onCanvasSizeChanged = onCanvasSizeChanged
      )

      Pickers(
        canvasState = canvasState,
        onCustomColorClick = onCustomColorClick,
        onFastColorClicked = onFastColorClicked,
        onColorChanged = onColorChanged,
        changeBrushSize = changeBrushSize,
        selectShape = selectShape
      )

      var generateDummyFramesDialogVisible by remember {
        mutableStateOf(false)
      }

      if (generateDummyFramesDialogVisible) {
        var dummyFramesToGenerate by remember {
          mutableStateOf("")
        }

        val isNumberEntered by remember {
          derivedStateOf {
            dummyFramesToGenerate.toIntOrNull() != null
          }
        }

        AlertDialog(
          onDismissRequest = {
            generateDummyFramesDialogVisible = false
          },
          confirmButton = {
            TextButton(
              enabled = isNumberEntered,
              onClick = {
                confirmGenerateDummyFrames(dummyFramesToGenerate.toInt())
                generateDummyFramesDialogVisible = false
              }
            ) {
              Text("Создать")
            }
          },
          title = {
            Text("Генерация кадров")
          },
          text = {
            Column(
              verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              Text("Введите число новых кадров с генерацией случайного контента")
              TextField(
                modifier = Modifier,
                value = dummyFramesToGenerate,
                onValueChange = {
                  dummyFramesToGenerate = it
                },
                keyboardOptions = KeyboardOptions(
                  keyboardType = KeyboardType.Number
                )
              )
            }
          },
        )
      }

      if (canvasState.framesSheetVisible) {
        ModalBottomSheet(
          modifier = Modifier
            .statusBarsPadding(),
          sheetState = rememberModalBottomSheetState(),
          windowInsets = BottomSheetDefaults.windowInsets.only(WindowInsetsSides.Bottom),
          onDismissRequest = {
            hideFrames()
          },
        ) {
          FramesScreen(
            modifier = Modifier
              .fillMaxSize(),
//            frames = canvasState.frames,
            selectFrame = selectFrame,
            deleteFrame = { index ->
              deleteFrame(Index(index))
            },
            deleteAllFrames = deleteAllFrames,
            canvasState = canvasState,
            generateDummyFrames = {
              generateDummyFramesDialogVisible = true
            }
          )
        }
      }
    }
  }
}

@Composable
private fun BackHandlers(
  canvasState: CanvasState,
  stopAnimation: () -> Unit,
  onColorClick: () -> Unit,
  onBrushSizeClick: () -> Unit,
  onShapeClick: () -> Unit,
) {
  BackHandler(
    enabled = canvasState.editorConfiguration.isPreviewAnimation
  ) {
    stopAnimation()
  }

  PickersBackHandlers(
    canvasState = canvasState,
    onColorClick = onColorClick,
    onBrushSizeClick = onBrushSizeClick,
    onShapeClick = onShapeClick
  )
}

@Composable
private fun PickersBackHandlers(
  canvasState: CanvasState,
  onColorClick: () -> Unit,
  onBrushSizeClick: () -> Unit,
  onShapeClick: () -> Unit,
) {
  BackHandler(
    enabled = canvasState.editorConfiguration.colorPickerVisible
  ) {
    // TODO заменить на hideColorPicker
    onColorClick()
  }

  BackHandler(
    enabled = canvasState.editorConfiguration.brushSizePickerVisible
  ) {
    // TODO заменить на hideBrushSizePicker
    onBrushSizeClick()
  }

  BackHandler(
    enabled = canvasState.editorConfiguration.shapesPickerVisible
  ) {
    // TODO заменить на hideShapePicker
    onShapeClick()
  }
}

@Composable
private fun Content(
  canvasState: CanvasState,
  undoChange: () -> Unit,
  redoChange: () -> Unit,
  deleteFrame: (FrameIndex) -> Unit,
  addFrame: () -> Unit,
  stopAnimation: () -> Unit,
  startAnimation: () -> Unit,
  copyFrame: () -> Unit,
  showFrames: () -> Unit,
  modifier: Modifier,
  onDragStart: () -> Unit,
  onDragEnd: (PathWithProperties) -> Unit,
  onDelayChanged: (Float) -> Unit,
  onPencilClick: () -> Unit,
  onEraseClick: () -> Unit,
  onColorClick: () -> Unit,
  onBrushSizeClick: () -> Unit,
  onShapeClick: () -> Unit,
  exportToGif: () -> Unit,
  onTransformModeClick: () -> Unit,
  onCanvasSizeChanged: (IntSize) -> Unit,
) {
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
        deleteFrame(Current)
      },
      addFrame = addFrame,
      stopAnimation = stopAnimation,
      startAnimation = startAnimation,
      copyFrame = copyFrame,
      canUndo = canvasState.canUndo,
      canRedo = canvasState.canRedo,
      showFrames = showFrames
    )

    val canvasBackground = ImageBitmap.imageResource(R.drawable.canvas)
    Canvas(
      modifier = modifier
        .weight(1f)
        .fillMaxWidth()
        .padding(16.dp)
        .clip(RoundedCornerShape(32.dp))
        .onSizeChanged {
          onCanvasSizeChanged(it)
        },
      canvasState = canvasState,
      onDragStart = onDragStart,
      onDragEnd = onDragEnd,
      backgroundModifier = Modifier
        .clip(RoundedCornerShape(32.dp))
        .drawBehind {
          drawImage(canvasBackground, dstSize = IntSize(size.width.toInt(), size.height.toInt()))
        }
    )

    BottomControls(
      canvasState = canvasState,
      onDelayChanged = onDelayChanged,
      onPencilClick = onPencilClick,
      onEraseClick = onEraseClick,
      onColorClick = onColorClick,
      onBrushSizeClick = onBrushSizeClick,
      onShapeClick = onShapeClick,
      exportToGif = exportToGif,
      onTransformModeClick = onTransformModeClick
    )
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BottomControls(
  canvasState: CanvasState,
  onDelayChanged: (Float) -> Unit,
  onPencilClick: () -> Unit,
  onEraseClick: () -> Unit,
  onColorClick: () -> Unit,
  onBrushSizeClick: () -> Unit,
  onShapeClick: () -> Unit,
  exportToGif: () -> Unit,
  onTransformModeClick: () -> Unit,
) {
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
          onClick = {
            exportToGif()
          }
        ) {
          Icon(
            imageVector = Icons.Default.Share,
            contentDescription = null,
          )
        }
      }
    } else {
      ru.kartollika.yandexcup.canvas.compose.controls.BottomControls(
        modifier = Modifier
          .fillMaxSize()
          .padding(horizontal = 8.dp),
        editorConfiguration = canvasState.editorConfiguration,
        onPencilClick = onPencilClick,
        onEraseClick = onEraseClick,
        onColorClick = onColorClick,
        onBrushSizeClick = onBrushSizeClick,
        onShapesClick = onShapeClick,
        onTransformModeClick = onTransformModeClick
      )
    }
  }
}

@Composable
private fun BoxScope.Pickers(
  canvasState: CanvasState,
  onCustomColorClick: () -> Unit,
  onFastColorClicked: (Color) -> Unit,
  onColorChanged: (Color) -> Unit,
  changeBrushSize: (Float) -> Unit,
  selectShape: (Shape) -> Unit = {},
) {
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
          tint = if (canvasState.editorConfiguration.colorPickerExpanded) {
            MaterialTheme.colorScheme.primary
          } else {
            Color.White
          }
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
        .height(60.dp)
        .width(250.dp)
        .noIndicationClickable(),
      editorConfiguration = canvasState.editorConfiguration,
      changeBrushSize = changeBrushSize
    )
  }

  if (canvasState.editorConfiguration.shapesPickerVisible) {
    InstrumentsPicker(
      modifier = Modifier
        .padding(bottom = 80.dp)
        .navigationBarsPadding()
        .align(Alignment.BottomCenter),
      selectShape = selectShape
    )
  }
}

@Composable
private fun Canvas(
  canvasState: CanvasState,
  modifier: Modifier = Modifier,
  onDragStart: () -> Unit = {},
  onDragEnd: (PathWithProperties) -> Unit = {},
  backgroundModifier: Modifier = Modifier,
) {
  val canvasDrawUiState = rememberCanvasDrawState()
  SideEffect {
    canvasDrawUiState.editorConfiguration = canvasState.editorConfiguration
  }

  SideEffect {
    canvasDrawUiState.mode = when (canvasState.editorConfiguration.currentMode) {
      Erase -> Draw
      Pencil -> Draw
      Transform -> CanvasMode.Transform
    }
  }

  LaunchedEffect(canvasState.currentFrame.paths) {
    canvasDrawUiState.currentPath = canvasState.currentFrame.paths?.lastOrNull()
  }

  DrawingCanvas(
    paths = {
      val paths = canvasState.currentFrame.paths ?: return@DrawingCanvas null
      paths.subList(0, (paths.size).coerceAtLeast(0))
    },
    previousPaths = {
      if (canvasState.editorConfiguration.isPreviewAnimation) return@DrawingCanvas null
      canvasState.previousFrame?.paths
    },
    modifier = modifier,
    onDragStart = {
      if (canvasState.editorConfiguration.isPreviewAnimation) return@DrawingCanvas
      canvasDrawUiState.startDrawing(it)
      onDragStart()
    },
    onDrag = {
      canvasDrawUiState.draw(it)
    },
    onDragEnd = {
      canvasDrawUiState.currentPath?.let(onDragEnd)
    },
    onDragCancel = {
      canvasDrawUiState.currentPath = null
    },
    canvasDrawUiState = canvasDrawUiState,
    onTransform = { centroid, pan, zoom, rotation ->
      val matrix = Matrix().apply {
        val cos = cos(Math.toRadians(rotation.toDouble()))
        val sin = sin(Math.toRadians(rotation.toDouble()))

        this.translate(
          x = (centroid.x * (1f - zoom) + (1 - cos) + centroid.y * sin).toFloat(),
          y = (centroid.y * (1f - zoom) + (1 - cos) - centroid.x * sin).toFloat()
        )
        this.scale(zoom, zoom)
        this.rotateZ(rotation)
        this.translate(pan.x, pan.y)
      }
      canvasDrawUiState.transform(matrix)
    },
    backgroundModifier = backgroundModifier
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