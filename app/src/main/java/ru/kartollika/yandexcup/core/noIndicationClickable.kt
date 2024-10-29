package ru.kartollika.yandexcup.core

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@Composable
fun Modifier.noIndicationClickable(
  enabled: Boolean = true,
  onClick: () -> Unit,
): Modifier {
  val interactionSource = remember { NoopMutableInteractionSource }

  return this then Modifier
      .clickable(
          enabled = enabled,
          indication = null,
          onClick = onClick,
          interactionSource = interactionSource
      )
}
