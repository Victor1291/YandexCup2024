package ru.kartollika.yandexcup.canvas.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun ColorsPicker(
  modifier: Modifier = Modifier,
  smallPickerColors: ImmutableList<Color> = persistentListOf(),
  colorItem: @Composable (Color) -> Unit = {},
) {
  Row(
    modifier = modifier,
    horizontalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterHorizontally),
  ) {
    smallPickerColors.forEach { color ->
      colorItem(color)
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