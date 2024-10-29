package ru.kartollika.yandexcup.canvas.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation.Horizontal
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import ru.kartollika.yandexcup.canvas.mvi.EditorConfiguration

@Composable
fun ColorsPicker(
  modifier: Modifier = Modifier,
  editorConfiguration: EditorConfiguration,
  smallPickerColors: ImmutableList<Color> = persistentListOf(),
  customColorItem: @Composable() (() -> Unit)? = null,
  fastColorClicked: (Color) -> Unit = {},
  pickColor: (Color) -> Unit = {},
) {
  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    AnimatedVisibility(
      modifier = Modifier,
      visible = editorConfiguration.colorPickerExpanded,
      enter = expandIn(
        clip = false,
        expandFrom = Alignment.TopStart,
        animationSpec = tween(200),
      ) + fadeIn()
    ) {
      Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        var maxOffset by remember { mutableStateOf(0f) }

        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(30.dp)
            .clip(CircleShape)
            .drawWithCache {
              val gradient = Brush.horizontalGradient(
                0f to Color.Red,
                1 / 3f to Color.Green,
                2 / 3f to Color.Blue,
                1f to Color.Red,
              )
              onDrawBehind {
                drawRect(gradient)
              }
            }
            .onPlaced {
              maxOffset = it.size.width.toFloat()
            }
        ) {
          var indicatorWidth by remember { mutableStateOf(0f) }
          val maxDrag = remember(maxOffset, indicatorWidth) {
            maxOffset - indicatorWidth
          }

          var dragOffset by remember { mutableStateOf(0f) }
          val draggable = rememberDraggableState { delta ->
            dragOffset += delta
            dragOffset = dragOffset.coerceIn(0f, maxDrag)
          }

          LaunchedEffect(maxDrag) {
            snapshotFlow { dragOffset }
              .collect { offset ->
                if (maxDrag == 0f) return@collect

                val hue = (offset / maxDrag) * 360
                val color = Color.hsl(hue, 1f, 0.5f)
                pickColor(color)
              }
          }

          Box(
            modifier = Modifier
              .size(30.dp)
              .onPlaced {
                indicatorWidth = it.size.width.toFloat()
              }
              .graphicsLayer {
                translationX = dragOffset
              }
              .border(width = 2.dp, Color.White, CircleShape)
              .draggable(
                state = draggable,
                orientation = Horizontal
              )
          )
        }
      }
    }

    Row(
      modifier = Modifier
        .fillMaxWidth()
        .background(Color.Gray, RoundedCornerShape(4.dp))
        .padding(16.dp),
      horizontalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterHorizontally),
    ) {
      customColorItem?.invoke()

      smallPickerColors.forEach { color ->
        ColorItem(
          color = color,
          modifier = Modifier
            .size(32.dp)
            .clip(CircleShape),
          onPick = {
            fastColorClicked(color)
          }
        )
      }
    }
  }
}

@Composable
fun ColorItem(
  color: Color,
  modifier: Modifier = Modifier,
  onPick: () -> Unit = {},
) {
  Spacer(
    modifier = modifier
      .clickable { onPick() }
      .background(color)
  )
}