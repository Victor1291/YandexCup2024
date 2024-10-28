package ru.kartollika.yandexcup.canvas.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Controls(
  modifier: Modifier = Modifier,
  startControls: @Composable() (RowScope.() -> Unit) = {},
  centerControls: @Composable() (RowScope.() -> Unit) = {},
  endControls: @Composable() (RowScope.() -> Unit) = {},
  startSpacedBy: Dp = 8.dp,
  centerSpacedBy: Dp = startSpacedBy,
  endSpacedBy: Dp = startSpacedBy,
) {
  Row(
    modifier = modifier,
    horizontalArrangement = Arrangement.SpaceBetween,
  ) {
    Row(
      horizontalArrangement = Arrangement.spacedBy(startSpacedBy)
    ) {
      startControls()
    }

    Row(
      horizontalArrangement = Arrangement.spacedBy(centerSpacedBy)
    ) {
      centerControls()
    }

    Row(
      horizontalArrangement = Arrangement.spacedBy(endSpacedBy)
    ) {
      endControls()
    }
  }
}