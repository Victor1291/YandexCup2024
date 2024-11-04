package ru.kartollika.yandexcup.canvas.compose.controls

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Controls(
  modifier: Modifier = Modifier,
  startControls: @Composable (RowScope.() -> Unit) = {},
  centerControls: @Composable (RowScope.() -> Unit) = {},
  endControls: @Composable (RowScope.() -> Unit) = {},
  startSpacedBy: Dp = 8.dp,
  centerSpacedBy: Dp = startSpacedBy,
  endSpacedBy: Dp = startSpacedBy,
) {
  Box(
    modifier = modifier,
  ) {
    Row(
      modifier = Modifier.align(Alignment.CenterStart),
      horizontalArrangement = Arrangement.spacedBy(startSpacedBy)
    ) {
      startControls()
    }

    Row(
      modifier = Modifier.align(Alignment.Center),
      horizontalArrangement = Arrangement.spacedBy(centerSpacedBy)
    ) {
      centerControls()
    }

    Row(
      modifier = Modifier.align(Alignment.CenterEnd),
      horizontalArrangement = Arrangement.spacedBy(endSpacedBy)
    ) {
      endControls()
    }
  }
}