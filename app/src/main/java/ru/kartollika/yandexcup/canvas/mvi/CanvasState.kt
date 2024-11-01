package ru.kartollika.yandexcup.canvas.mvi

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import ru.kartollika.yandexcup.mvi2.MVIState

@Immutable
data class CanvasState(
  val frames: Frames = persistentListOf(RealFrame()),
  val currentFrameIndex: Int = 0,
  val editorConfiguration: EditorConfiguration = EditorConfiguration(),
  val framesSheetVisible: Boolean = false,
) : MVIState {

  val canUndo: Boolean
    get() = currentFrame.historyIndex > 0

  val canRedo: Boolean
    get() {
      val snapshots = currentFrame.snapshots ?: return false
      return currentFrame.historyIndex < snapshots.lastIndex
    }

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
  data object Move : DrawMode
}

typealias Frames = ImmutableList<Frame>

sealed interface Frame {
  val paths: ImmutableList<PathWithProperties>?
  val previousSnapshot: FrameSnapshot?
  val nextSnapshot: FrameSnapshot?
  val historyIndex: Int
  val snapshots: ImmutableList<FrameSnapshot>?

  fun materialize(): RealFrame
}

data object GhostFrame: Frame {

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
}

@Immutable
data class RealFrame(
  override val paths: ImmutableList<PathWithProperties>? = persistentListOf(),
  override val historyIndex: Int = 0,
  override val snapshots: ImmutableList<FrameSnapshot>? = persistentListOf(FrameSnapshot()),
) : Frame {
  override val previousSnapshot: FrameSnapshot?
    get() = snapshots?.getOrNull(historyIndex - 1)

  override val nextSnapshot: FrameSnapshot?
    get() = snapshots?.getOrNull(historyIndex + 1)

  override fun materialize(): RealFrame = this
}

fun RealFrame.restoreSnapshot(snapshot: FrameSnapshot): Frame = copy(
  paths = snapshot.paths,
  historyIndex = snapshot.snapshotIndex
)

fun ImmutableList<FrameSnapshot>.dropSnapshotsStartingFrom(index: Int): ImmutableList<FrameSnapshot> {
  return subList(0, index)
}

fun ImmutableList<FrameSnapshot>.pushSnapshot(frame: Frame): ImmutableList<FrameSnapshot> {
  return toMutableList().apply {
    add(
      FrameSnapshot(
        paths = frame.paths!!,
        snapshotIndex = frame.historyIndex + 1
      )
    )
  }.toImmutableList()
}

@Immutable
data class FrameSnapshot(
  val paths: ImmutableList<PathWithProperties> = persistentListOf(),
  val snapshotIndex: Int = 0,
)

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