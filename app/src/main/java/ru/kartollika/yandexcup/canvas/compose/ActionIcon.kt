package ru.kartollika.yandexcup.canvas.compose

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

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
      modifier = Modifier.padding(1.dp),
      painter = painterResource(icon),
      contentDescription = contentDescription,
    )
  }
}

@Composable
fun ActionIcon(
  icon: ImageVector,
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
      imageVector = icon,
      contentDescription = contentDescription,
    )
  }
}