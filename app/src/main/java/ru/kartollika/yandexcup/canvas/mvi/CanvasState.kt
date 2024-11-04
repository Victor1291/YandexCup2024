package ru.kartollika.yandexcup.canvas.mvi

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.IntSize
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import ru.kartollika.yandexcup.canvas.mvi.DrawMode.Erase
import ru.kartollika.yandexcup.mvi2.MVIState

@Immutable
data class CanvasState(
  val currentFrameIndex: Int = 0,
  val editorConfiguration: EditorConfiguration = EditorConfiguration(),
  val framesSheetVisible: Boolean = false,
  val maxFramesCount: Int = 1,
  val currentFrame: RealFrame,
  val previousFrame: RealFrame? = null,
  val gifExportProcessed: Int? = null,
) : MVIState {

  val canUndo: Boolean
    get() = currentFrame.paths.isNotEmpty()

  val canRedo: Boolean
    get() = currentFrame.undoPaths?.isNotEmpty() == true
}

@Immutable
data class PathWithProperties(
  val path: Path,
  val properties: PathProperties,
  val drawIndex: Int = 0,
)

@Immutable
data class PathProperties(
  val color: Color,
  val eraseMode: Boolean = false,
  val brushSize: Float,
)

@Immutable
sealed interface DrawMode {
  @Immutable
  data object Pencil : DrawMode

  @Immutable
  data object Erase : DrawMode

  @Immutable
  data object Transform : DrawMode
}

sealed interface Frame {
  fun materialize(): Frame
}

data class GhostFrame(
  private val creator: () -> Frame = { RealFrame() },
) : Frame {
  private var materializedFrame: Frame? = null

  override fun materialize(): Frame {
    val materializedFrame = materializedFrame
    if (materializedFrame != null) {
      return materializedFrame
    }
    return creator().also {
      this.materializedFrame = it
    }
  }
}

@Immutable
data class RealFrame(
  val paths: ImmutableList<PathWithProperties> = persistentListOf(),
  val undoPaths: ImmutableList<PathWithProperties>? = null,
) : Frame {
  override fun materialize(): Frame = this
}

fun RealFrame.getOrCreateUndoPaths() = undoPaths.takeIf { it != null } ?: persistentListOf()

@Immutable
data class EditorConfiguration(
  val isPreviewAnimation: Boolean = false,
  val color: Color = Color.Blue,
  val colorPickerVisible: Boolean = false,
  val colorPickerExpanded: Boolean = false,
  val brushSizePickerVisible: Boolean = false,
  val currentMode: DrawMode = DrawMode.Pencil,
  val animationDelay: Int = 200,
  val brushSize: Float = 30f,
  val eraserSize: Float = 30f,
  val shapesPickerVisible: Boolean = false,
  val dummyFramesCountInputVisible: Boolean = false,
  val canvasSize: IntSize = IntSize(0, 0),
  val isLoading: Boolean = false,
) {
  val brushSizeByMode: Float
    get() = if (currentMode == Erase) eraserSize else brushSize
}