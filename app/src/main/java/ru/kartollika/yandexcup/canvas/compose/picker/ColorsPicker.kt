package ru.kartollika.yandexcup.canvas.compose.picker

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.draggable2D
import androidx.compose.foundation.gestures.rememberDraggable2DState
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
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.collectLatest
import ru.kartollika.yandexcup.canvas.mvi.EditorConfiguration
import ru.kartollika.yandexcup.ui.theme.YandexCup2024Theme

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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HslPicker(
  modifier: Modifier = Modifier,
  onPickColor: (Color) -> Unit = {},
) {
  var colorPickerSize by remember { mutableStateOf(IntSize.Zero) }
  var dragOffset by remember { mutableStateOf(Offset.Zero) }
  val draggable = rememberDraggable2DState { offset ->
    val rawOffset = dragOffset + offset
    dragOffset = rawOffset.copy(
      x = rawOffset.x.coerceIn(0f, colorPickerSize.width.toFloat()),
      y = rawOffset.y.coerceIn(0f, colorPickerSize.width.toFloat()),
    )
  }

  LaunchedEffect(Unit) {
    snapshotFlow { dragOffset }
      .collectLatest {
        val hue = dragOffset.y * 360 / colorPickerSize.height
        val yPercent = dragOffset.x * 100 / colorPickerSize.width
        val light = when {
          yPercent < 30f -> {
            yPercent / 50f
          }

          yPercent > 75f -> {
            0.5f + (yPercent - 75f) / 50f
          }

          else -> 0.5f
        }

        println("light $light")
        val color = Color.hsl(hue, 1f, 1f - light)
        onPickColor(color)
      }
  }

  Box(
    modifier = modifier
      .drawBehind {
        drawRect(
          brush = Brush.verticalGradient(
            listOf(
              Color.Red,
              Color.Yellow,
              Color.Green,
              Color.Cyan,
              Color.Blue,
              Color.Magenta,
              Color.Red
            )
          )
        )

        drawRect(
          brush = Brush.horizontalGradient(
            listOf(
              Color.White,
              Color.Transparent,
              Color.Transparent,
              Color.Transparent,
              Color.Black
            )
          )
        )
      }
      .pointerInput(Unit) {
        detectTapGestures(
          onPress = {
            dragOffset = it
          }
        )
      }
      .draggable2D(
        state = draggable
      )
      .onSizeChanged {
        colorPickerSize = it
      }
  ) {
    Spacer(
      modifier = Modifier
        .graphicsLayer {
          translationX = -6.dp.toPx()
          translationY = -6.dp.toPx()
        }
        .graphicsLayer {
          translationX = dragOffset.x
          translationY = dragOffset.y
        }
        .border(3.dp, Color.White, CircleShape)
        .padding(12.dp)
    )
  }
}

@Preview
@Composable
private fun ColorHslPickerPreview() {
  YandexCup2024Theme {
    HslPicker(
      modifier = Modifier
        .fillMaxWidth()
        .height(300.dp)
    )
  }
}

@Preview
@Composable
private fun ColorPickerInModalSheet() {
  YandexCup2024Theme {
    var color by remember {
      mutableStateOf(Color.Unspecified)
    }

    Surface(
      modifier = Modifier
        .background(color)
        .fillMaxWidth()
        .height(400.dp)
        .padding(16.dp)
        .clip(RoundedCornerShape(16.dp))
    ) {
      HslPicker(
        modifier = Modifier
          .clip(RoundedCornerShape(16.dp)),
        onPickColor = {
          color = it
        }
      )
    }
  }
}