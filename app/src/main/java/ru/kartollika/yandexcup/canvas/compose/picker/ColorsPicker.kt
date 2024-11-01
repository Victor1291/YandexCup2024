package ru.kartollika.yandexcup.canvas.compose.picker

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import ru.kartollika.yandexcup.canvas.mvi.EditorConfiguration

@OptIn(ExperimentalMaterial3Api::class)
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
      ) + fadeIn(),
      exit = shrinkOut(
        clip = false,
        shrinkTowards = Alignment.TopStart,
        animationSpec = tween(200),
      ) + fadeOut()
    ) {
      Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        var value by remember { mutableFloatStateOf(0f) }

        LaunchedEffect(Unit) {
          snapshotFlow { value }
            .collect { offset ->
              val hue = offset * 360
              val color = Color.hsl(hue, 1f, 0.5f)
              pickColor(color)
            }
        }

        Slider(
          value = value,
          onValueChange = {
            value = it
          },
          thumb = {
            Box(
              modifier = Modifier
                .size(30.dp)
                .border(width = 2.dp, Color.White, CircleShape)
            )
          },
          track = {
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .height(30.dp)
                .scale(scaleX = 1.11f, scaleY = 1f)
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
            )
          })
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