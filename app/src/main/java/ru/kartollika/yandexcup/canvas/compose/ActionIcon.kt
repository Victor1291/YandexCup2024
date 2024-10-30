package ru.kartollika.yandexcup.canvas.compose

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource

@Composable
fun ActionIcon(
  @DrawableRes icon: Int,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  tint: Color = LocalContentColor.current,
  disabledAlpha: Float = 0.3f,
  contentDescription: String? = null,
) {
  Icon(
    modifier = modifier
      .clip(CircleShape)
      .clickable(enabled = enabled) {
        onClick()
      }
      .graphicsLayer {
        alpha = if (enabled) 1f else disabledAlpha
      },
    painter = painterResource(icon),
    tint = tint,
    contentDescription = contentDescription,
  )
}