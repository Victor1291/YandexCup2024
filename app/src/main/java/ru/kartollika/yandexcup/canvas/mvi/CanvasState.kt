package ru.kartollika.yandexcup.canvas.mvi

import androidx.compose.runtime.Immutable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import ru.kartollika.yandexcup.mvi2.MVIState

@Immutable
data class CanvasState(
  val frames: Frames = persistentListOf(Frame()),
  val currentFrameIndex: Int = 0,
  val editorConfiguration: EditorConfiguration = EditorConfiguration(),
  val framesSheetVisible: Boolean = false,
) : MVIState {

  val canUndo: Boolean
    get() = currentFrame.paths.isNotEmpty()

  val canRedo: Boolean
    get() = currentFrame.undoPaths.isNotEmpty()

  val currentFrame: Frame
    get() = frames[currentFrameIndex]

  val previousFrame: Frame?
    get() = frames.getOrNull(currentFrameIndex - 1)
}

@Immutable
data class PathWithProperties(
  val path: Path,
  val properties: PathProperties,
)

@Immutable
data class PathProperties(
  val color: Color,
  val eraseMode: Boolean = false,
)

@Immutable
sealed interface DrawMode {
  @Immutable
  data object Pencil : DrawMode

  @Immutable
  data object Erase : DrawMode
}

typealias Frames = ImmutableList<Frame>

@Immutable
data class Frame(
  val paths: ImmutableList<PathWithProperties> = persistentListOf(),
  val currentPath: PathWithProperties? = null,
  val lastOffset: Offset = Offset.Unspecified,
  val undoPaths: ImmutableList<PathWithProperties> = persistentListOf(),
)

@Immutable
data class EditorConfiguration(
  val isPreviewAnimation: Boolean = false,
  val color: Color = Color.Blue,
  val colorPickerVisible: Boolean = false,
  val currentMode: DrawMode = DrawMode.Pencil,
  val animationDelay: Int = 200
)