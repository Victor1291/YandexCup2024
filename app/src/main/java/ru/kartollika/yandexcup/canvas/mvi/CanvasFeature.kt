package ru.kartollika.yandexcup.canvas.mvi

import android.util.Log
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.kartollika.yandexcup.canvas.Shape
import ru.kartollika.yandexcup.canvas.addNewFrame
import ru.kartollika.yandexcup.canvas.copyFrame
import ru.kartollika.yandexcup.canvas.deleteAllFrames
import ru.kartollika.yandexcup.canvas.deleteFrame
import ru.kartollika.yandexcup.canvas.hidePickers
import ru.kartollika.yandexcup.canvas.mutateFrames
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.AddFrames
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.AddNewFrame
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.AnimationDelayChange
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.CanvasMeasured
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.ChangeBrushSize
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.ChangeCurrentFrame
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.CopyFrame
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.CustomColorClick
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.DeleteAllFrames
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.DeleteFrame
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.DrawFinish
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.DrawStart
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.EraseClick
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.ExportToGif
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.GenerateDummyFrames
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.HideBrushSizePicker
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.HideColorPicker
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.HideFrames
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.OnColorChanged
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.OnColorClick
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.OnColorItemClicked
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.OpenShapes
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.PencilClick
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.RedoChange
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.SelectFrame
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.SelectShape
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.ShowBrushSizePicker
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.ShowColorPicker
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.ShowFrames
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.StartAnimation
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.StopAnimation
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.TransformModeClick
import ru.kartollika.yandexcup.canvas.mvi.CanvasAction.UndoChange
import ru.kartollika.yandexcup.canvas.mvi.DrawMode.Erase
import ru.kartollika.yandexcup.canvas.mvi.DrawMode.Pencil
import ru.kartollika.yandexcup.canvas.mvi.DrawMode.Transform
import ru.kartollika.yandexcup.canvas.openBrushPicker
import ru.kartollika.yandexcup.canvas.openColorPicker
import ru.kartollika.yandexcup.canvas.openShapesPicker
import ru.kartollika.yandexcup.canvas.sources.DummyPathsGenerator
import ru.kartollika.yandexcup.canvas.sources.EditorConfigurationParser
import ru.kartollika.yandexcup.canvas.sources.GifExporter
import ru.kartollika.yandexcup.canvas.sources.ShapeDrawer
import ru.kartollika.yandexcup.canvas.updateEditorConfig
import ru.kartollika.yandexcup.core.replace
import ru.kartollika.yandexcup.mvi2.MVIFeature
import javax.inject.Inject
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.milliseconds

class CanvasFeature @Inject constructor(
  private val gifExporter: GifExporter,
  private val dummyPathsGenerator: DummyPathsGenerator,
  private val editorConfigurationParser: EditorConfigurationParser,
  private val shapeDrawer: ShapeDrawer,
) : MVIFeature<CanvasState, CanvasAction, CanvasEvent>() {
  override fun initialState(): CanvasState = CanvasState()

  override suspend fun processAction(state: CanvasState, action: CanvasAction) {
    when (action) {
      is DrawStart -> Unit
      is StartAnimation -> {
        startFramesAnimation()
      }

      is DrawFinish -> Unit
      is EraseClick -> {
        consumeAction(HideColorPicker)
      }

      is OnColorClick -> Unit
      is PencilClick -> {
        consumeAction(HideColorPicker)
      }

      is UndoChange -> Unit
      is RedoChange -> Unit
      is OnColorChanged -> Unit
      is AddNewFrame -> Unit
      is DeleteFrame -> Unit
      is StopAnimation -> Unit
      is ChangeCurrentFrame -> Unit
      is AnimationDelayChange -> Unit
      CopyFrame -> Unit
      ShowFrames -> Unit
      HideFrames -> Unit
      is SelectFrame -> Unit
      is DeleteAllFrames -> Unit
      is HideColorPicker -> Unit
      ShowBrushSizePicker -> Unit
      is ChangeBrushSize -> Unit
      HideBrushSizePicker -> Unit
      CustomColorClick -> Unit
      ShowColorPicker -> Unit

      is OnColorItemClicked -> {
        consumeAction(HideColorPicker)
      }

      OpenShapes -> Unit
      is SelectShape -> drawShape(action.shape)
      is ExportToGif -> processExportToGif()
//      is GenerateDummyFrames -> generateDummyFrames(action.framesCount)
      is GenerateDummyFrames -> Unit
      is AddFrames -> Unit
      TransformModeClick -> Unit
      is CanvasMeasured -> Unit
    }
  }

  private fun mockDummyFrames(framesCount: Int) {
  }

  private suspend fun generateDummyFrames(framesCount: Int) = coroutineScope {
    launch(Dispatchers.Default) {
      var processed = 0

      try {
        (0 until framesCount)
          .asSequence()
          .chunked(1000000)
          .map { it.size }
          .forEach { chunkSize ->
            val frames = dummyPathsGenerator.generateFrames(
              framesCount = chunkSize,
              editorConfiguration = state.value.editorConfiguration
            )
            consumeAction(AddFrames(frames))
            processed += chunkSize
            println("dummy creator: processed $processed")
          }
      } catch (e: Exception) {
        Log.e("dummy creator", "Error", e)
        println("dummy creator: processed with error $processed")
      }

      println("dummy creator: processed $processed")
    }
  }

  private suspend fun processExportToGif() {
    gifExporter.export(
      fileName = "animation.gif",
      frames = state.value.frames,
      canvasSize = state.value.editorConfiguration.canvasSize,
    )
  }

  private fun drawShape(shape: Shape) {
    val path = shapeDrawer.drawShape(shape)

    consumeAction(
      DrawFinish(
        PathWithProperties(
          path = path,
          properties = editorConfigurationParser.parseToProperties(state.value.editorConfiguration)
        )
      )
    )
  }

  private suspend fun startFramesAnimation() = coroutineScope {
    launch {
      var frameIndex = 0
      while (state.value.editorConfiguration.isPreviewAnimation) {
        consumeAction(ChangeCurrentFrame(frameIndex))
        delay(state.value.editorConfiguration.animationDelay.milliseconds)
        frameIndex = (frameIndex + 1) % state.value.frames.size
      }
    }
  }

  override fun reduce(state: CanvasState, action: CanvasAction): CanvasState {
    return when (action) {
      is DrawStart -> state.hidePickers()
      is DrawFinish -> {
        state.copy(
          frames = state.frames.replace(
            replaceIndex = state.currentFrameIndex,
            newItem = { frame: Frame ->
              val paths = frame.paths.toMutableList().apply {
                action.pathWithProperties.let(this::add)
              }.toImmutableList()

              frame.copy(
                paths = paths,
                undoPaths = null,
              )
            }
          ).toImmutableList()
        )
      }

      EraseClick -> state.updateEditorConfig(
        currentMode = Erase
      )

      is OnColorClick -> if (state.editorConfiguration.colorPickerVisible) {
        state.hidePickers()
      } else {
        state.openColorPicker()
      }

      PencilClick -> state.updateEditorConfig(
        currentMode = Pencil
      )

      UndoChange -> {
        state.copy(
          frames = state.frames.replace(
            replaceIndex = state.currentFrameIndex,
            newItem = { frame ->
              val pathToUndo = frame.paths.last()
              frame.copy(
                paths = frame.paths.subList(0, frame.paths.lastIndex),
                undoPaths = (frame.getOrCreateUndoPaths().plus(pathToUndo)).toImmutableList()
              )
            }
          ).toImmutableList()
        )
      }

      RedoChange -> {
        state.copy(
          frames = state.frames.replace(
            replaceIndex = state.currentFrameIndex,
            newItem = { frame ->
              frame.copy(
                paths = (frame.paths + frame.undoPaths!!.last()).toImmutableList(),
                undoPaths = frame.undoPaths.subList(0, frame.undoPaths.lastIndex)
                  .takeIf { it.isNotEmpty() }
              )
            }
          ).toImmutableList(),
        )
      }

      is OnColorChanged -> state.updateEditorConfig(
        color = action.color,
      )

      AddNewFrame -> state.addNewFrame()
      is DeleteFrame -> state.deleteFrame(action.frameIndex)

      StartAnimation -> {
        state.updateEditorConfig(
          isPreviewAnimation = true
        ).hidePickers()
      }

      StopAnimation -> state.copy(
        editorConfiguration = state.editorConfiguration.copy(
          isPreviewAnimation = false,
        ),
        currentFrameIndex = state.frames.lastIndex
      )

      is ChangeCurrentFrame -> state.copy(
        currentFrameIndex = action.frameIndex
      )

      is AnimationDelayChange -> state.updateEditorConfig(
        animationDelay = action.animationDelay.roundToInt()
      )

      CopyFrame -> state.copyFrame()

      ShowFrames -> state.copy(
        framesSheetVisible = true
      )

      HideFrames -> state.copy(
        framesSheetVisible = false
      )

      is SelectFrame -> state.copy(
        currentFrameIndex = action.frameIndex,
        framesSheetVisible = false
      )

      DeleteAllFrames -> state.deleteAllFrames()

      is ShowColorPicker -> state.openColorPicker()
      is ShowBrushSizePicker -> {
        if (state.editorConfiguration.brushSizePickerVisible) {
          state.hidePickers()
        } else {
          state.openBrushPicker()
        }
      }

      is OpenShapes -> {
        if (state.editorConfiguration.shapesPickerVisible) {
          state.hidePickers()
        } else {
          state.openShapesPicker()
        }
      }

      is HideColorPicker -> state.hidePickers()
      HideBrushSizePicker -> state.hidePickers()

      is ChangeBrushSize -> {
        val updatedState = state.updateEditorConfig(
          brushSize = action.size,
        )

        if (state.editorConfiguration.currentMode != Transform) return updatedState

        return updatedState.copy(
          frames = state.frames.replace(
            replaceIndex = state.currentFrameIndex,
            newItem = { frame ->
              val paths = frame.paths
              frame.copy(
                paths = paths.replace(
                  replaceIndex = paths.lastIndex,
                  newItem = { path ->
                    path.copy(
                      properties = path.properties.copy(
                        brushSize = updatedState.editorConfiguration.brushSize
                      )
                    )
                  }
                ).toImmutableList()
              )
            }
          ).toImmutableList()
        )
      }

      CustomColorClick -> state.updateEditorConfig(
        colorPickerExpanded = !state.editorConfiguration.colorPickerExpanded
      )

      is OnColorItemClicked -> state.updateEditorConfig(
        color = action.color,
        colorPickerExpanded = false
      )

      is SelectShape -> state.openBrushPicker().updateEditorConfig(
        currentMode = Transform
      )
      // TODO Отобразить диалог с лоадером
      ExportToGif -> state
      is GenerateDummyFrames -> state.copy(
        maxFramesCount = state.framesCount + action.framesCount
      )

      is AddFrames -> state.copy(
        frames = state.mutateFrames {
          addAll(action.frames)
        }
      )

      TransformModeClick -> state.updateEditorConfig(
        currentMode = Transform
      )

      is CanvasMeasured -> state.updateEditorConfig(
        canvasSize = action.size
      )
    }
  }
}