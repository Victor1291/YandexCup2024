package ru.kartollika.yandexcup.canvas.compose

import androidx.annotation.DrawableRes
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource

@Composable
fun ActionIcon(
  @DrawableRes icon: Int,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  tint: Color = LocalContentColor.current,
  contentDescription: String? = null,
) {
  IconButton(
    modifier = modifier,
    onClick = onClick,
    enabled = enabled,
    colors = IconButtonDefaults.iconButtonColors(
      contentColor = tint
    )
  ) {
    Icon(
      painter = painterResource(icon),
      contentDescription = contentDescription,
    )
  }
}