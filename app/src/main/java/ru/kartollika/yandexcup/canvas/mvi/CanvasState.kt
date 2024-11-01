package ru.kartollika.yandexcup.canvas.mvi

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import ru.kartollika.yandexcup.mvi2.MVIState
import kotlin.math.max

@Immutable
data class CanvasState(
  val frames: Frames = persistentListOf(Frame()),
  val currentFrameIndex: Int = 0,
  val editorConfiguration: EditorConfiguration = EditorConfiguration(),
  val framesSheetVisible: Boolean = false,
  val maxFramesCount: Int = 1,
) : MVIState {

  /**
   * Current count of frames
   * frames.size is actual and real frames
   * maxFramesCount may be a higher value because it imitates ghost frames for better performance
   */
  val framesCount = max(frames.size, maxFramesCount)

  val canUndo: Boolean
    get() = currentFrame.paths.isNotEmpty()

  val canRedo: Boolean
    get() = currentFrame.undoPaths?.isNotEmpty() == true

  val currentFrame: Frame
    get() = frames[currentFrameIndex]

  val previousFrame: Frame?
    get() = frames.getOrNull(currentFrameIndex - 1)
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

/*sealed interface Frame {
  val paths: ImmutableList<PathWithProperties>?
  val previousSnapshot: FrameSnapshot?
  val nextSnapshot: FrameSnapshot?
  val historyIndex: Int
  val snapshots: ImmutableList<FrameSnapshot>?

  fun materialize(): RealFrame
}*/

/*data object GhostFrame: Frame {

  private val creator: () -> RealFrame = { RealFrame() }

  override val paths: ImmutableList<PathWithProperties>? = null
  override val historyIndex: Int = 0
  override val snapshots: ImmutableList<FrameSnapshot>? = null
  override val previousSnapshot: FrameSnapshot? = null
  override val nextSnapshot: FrameSnapshot? = null

  override fun materialize(): RealFrame {
    return creator().copy(
      paths = persistentListOf(),
      snapshots = persistentListOf(FrameSnapshot())
    )
  }
}*/

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
  val brushSize: Float = 10f,
  val shapesPickerVisible: Boolean = false,
  val dummyFramesCountInputVisible: Boolean = false,
)