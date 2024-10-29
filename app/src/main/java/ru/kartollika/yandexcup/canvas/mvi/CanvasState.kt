package ru.kartollika.yandexcup.canvas.mvi

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import ru.kartollika.yandexcup.mvi2.MVIState

@Immutable
data class CanvasState(
  val frames: Frames = persistentListOf(Frame()),
  val currentFrameIndex: Int = 0,
  val editorConfiguration: EditorConfiguration = EditorConfiguration(),
  val framesSheetVisible: Boolean = false,
) : MVIState {

  val canUndo: Boolean
    get() = currentFrame.historyIndex > 0

  val canRedo: Boolean
    get() = currentFrame.historyIndex < currentFrame.snapshots.lastIndex

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
  val brushSize: Float,
)

@Immutable
sealed interface DrawMode {
  @Immutable
  data object Pencil : DrawMode

  @Immutable
  data object Erase : DrawMode
}

typealias Frames = ImmutableList<Frame>

@Stable
data class Frame(
  val paths: ImmutableList<PathWithProperties> = persistentListOf(),
  val currentPath: PathWithProperties? = null,
  val lastOffset: Offset = Offset.Unspecified,
  val historyIndex: Int = 0,
  val snapshots: ImmutableList<FrameSnapshot> = persistentListOf(FrameSnapshot()),
) {
  val previousSnapshot: FrameSnapshot?
    get() = snapshots.getOrNull(historyIndex - 1)

  val nextSnapshot: FrameSnapshot?
    get() = snapshots.getOrNull(historyIndex + 1)
}

fun Frame.restoreSnapshot(snapshot: FrameSnapshot): Frame = copy(
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
        paths = frame.paths,
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
)