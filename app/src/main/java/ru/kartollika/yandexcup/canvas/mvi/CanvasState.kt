package ru.kartollika.yandexcup.canvas.mvi

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.IntSize
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import ru.kartollika.yandexcup.mvi2.MVIState

@Immutable
data class CanvasState(
  val currentFrameIndex: Int = 0,
  val editorConfiguration: EditorConfiguration = EditorConfiguration(),
  val framesSheetVisible: Boolean = false,
  val maxFramesCount: Int = 1,
  val currentFrame: Frame,
  val previousFrame: Frame? = null
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

typealias Frames = ImmutableList<Frame>

@Immutable
data class Frame(
  val paths: ImmutableList<PathWithProperties> = persistentListOf(),
  val undoPaths: ImmutableList<PathWithProperties>? = null,
)

fun Frame.getOrCreateUndoPaths() = undoPaths.takeIf { it != null } ?: persistentListOf()

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
  val shapesPickerVisible: Boolean = false,
  val dummyFramesCountInputVisible: Boolean = false,
  val canvasSize: IntSize = IntSize(0, 0)
)