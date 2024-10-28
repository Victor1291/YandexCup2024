package ru.kartollika.yandexcup.canvas.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TopControls(
  modifier: Modifier = Modifier,
  startControls: @Composable() (RowScope.() -> Unit) = {},
  centerControls: @Composable() (RowScope.() -> Unit) = {},
  endControls: @Composable() (RowScope.() -> Unit) = {},
) {
  Row(
    modifier = modifier,
    horizontalArrangement = Arrangement.SpaceBetween,
  ) {
    Row(
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      startControls()
    }

    Row(
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      centerControls()
    }

    Row(
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      endControls()
    }
  }
}