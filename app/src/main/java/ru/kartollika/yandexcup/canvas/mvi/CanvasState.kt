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
  val currentMode: DrawMode = DrawMode.Pencil,
  val color: Color = Color.Blue,
  val paths: ImmutableList<PathWithProperties> = persistentListOf(),
  val currentPath: PathWithProperties? = null,
  val lastOffset: Offset = Offset.Unspecified,
  val undoPaths: ImmutableList<PathWithProperties> = persistentListOf(),
  val colorPickerVisible: Boolean = false,
) : MVIState {
  val canUndo: Boolean
    get() = paths.isNotEmpty()

  val canRedo: Boolean
    get() = undoPaths.isNotEmpty()
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
  data object Pencil : DrawMode
  data object Erase : DrawMode
}