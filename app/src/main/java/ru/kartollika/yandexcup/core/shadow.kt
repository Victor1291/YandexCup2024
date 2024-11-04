package ru.kartollika.yandexcup.core

import android.graphics.BlurMaskFilter
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Shadow modifier with drawing custom shadow using [androidx.compose.ui.graphics.Canvas]
 * Got from https://gist.github.com/Andrew0000/3edb9c25ebc20a2935c9ff4805e05f5d
 * And https://medium.com/@hanihashemi/implementing-custom-shadows-with-jetpack-compose-for-neumorphism-design-cd666887a642
 */
@Composable
fun Modifier.shadow(
  color: Color = Color.Black,
  offsetX: Dp = 0.dp,
  offsetY: Dp = 0.dp,
  blurRadius: Dp = 0.dp,
  shapeRadius: Dp = 0.dp,
): Modifier {
  // BlurMaskFilter requires to have blurRadius > 0
  if (blurRadius <= 0.dp) return this

  val paint: Paint = remember { Paint() }
  val blurRadiusPx = blurRadius.toPx(LocalDensity.current)
  val maskFilter = remember {
    BlurMaskFilter(blurRadiusPx, BlurMaskFilter.Blur.NORMAL)
  }

  return this then Modifier
      .drawBehind {
        drawIntoCanvas { canvas ->
          val frameworkPaint = paint.asFrameworkPaint()
          if (blurRadius != 0.dp) {
            frameworkPaint.maskFilter = maskFilter
          }
          frameworkPaint.color = color.toArgb()

          val leftPixel = offsetX.toPx()
          val topPixel = offsetY.toPx()
          val rightPixel = size.width + leftPixel
          val bottomPixel = size.height + topPixel

          if (shapeRadius > 0.dp) {
            val radiusPx = shapeRadius.toPx()
            canvas.drawRoundRect(
                left = leftPixel,
                top = topPixel,
                right = rightPixel,
                bottom = bottomPixel,
                radiusX = radiusPx,
                radiusY = radiusPx,
                paint = paint,
            )
          } else {
            canvas.drawRect(
                left = leftPixel,
                top = topPixel,
                right = rightPixel,
                bottom = bottomPixel,
                paint = paint,
            )
          }
        }
      }
}

@Preview(
    widthDp = 80,
    heightDp = 80,
    showBackground = true,
    backgroundColor = 0xfff,
)
@Composable
private fun BlackShadowRoundedPreview() {
  Box(
      modifier = Modifier
          .padding(16.dp)
          .shadow(
              blurRadius = 8.dp,
              shapeRadius = 8.dp
          )
          .background(Color.Green, RoundedCornerShape(8.dp)),
  )
}

@Preview(
    widthDp = 80,
    heightDp = 80,
    showBackground = true,
    backgroundColor = 0x000,
)
@Composable
private fun WhiteShadowRoundedPreview() {
  Box(
      modifier = Modifier
          .padding(16.dp)
          .shadow(
              color = Color.White,
              blurRadius = 8.dp,
              shapeRadius = 8.dp
          )
          .background(Color.Green, RoundedCornerShape(8.dp)),
  )
}

@Preview(
    widthDp = 80,
    heightDp = 80,
    showBackground = true,
    backgroundColor = 0xfff,
)
@Composable
private fun BlackShadowRectPreview() {
  Box(
      modifier = Modifier
          .padding(16.dp)
          .shadow(
              blurRadius = 4.dp,
              shapeRadius = 0.dp
          )
          .background(Color.Green, RoundedCornerShape(8.dp)),
  )
}

@Preview(
    widthDp = 80,
    heightDp = 80,
    showBackground = true,
    backgroundColor = 0x000,
)
@Composable
private fun WhiteShadowRectPreview() {
  Box(
      modifier = Modifier
          .padding(16.dp)
          .shadow(
              color = Color.White,
              blurRadius = 4.dp,
              shapeRadius = 0.dp
          )
          .background(Color.Green, RoundedCornerShape(8.dp)),
  )
}

@Preview(
    showBackground = true,
    backgroundColor = 0xfff,
)
@Composable
private fun ShadowsWithOffsetsPreview() {
  Column {
    Row(
        modifier = Modifier.height(64.dp),
    ) {
      Box(
          modifier = Modifier
              .aspectRatio(1f)
              .fillMaxHeight()
              .padding(16.dp)
              .shadow(
                  blurRadius = 8.dp,
                  shapeRadius = 8.dp,
                  offsetX = 8.dp,
                  offsetY = 8.dp
              )
              .background(Color.Green, RoundedCornerShape(8.dp)),
      )

      Box(
          modifier = Modifier
              .aspectRatio(1f)
              .fillMaxHeight()
              .padding(16.dp)
              .shadow(
                  blurRadius = 8.dp,
                  shapeRadius = 8.dp,
                  offsetX = 8.dp,
                  offsetY = -8.dp
              )
              .background(Color.Green, RoundedCornerShape(8.dp)),
      )

      Box(
          modifier = Modifier
              .aspectRatio(1f)
              .fillMaxHeight()
              .padding(16.dp)
              .shadow(
                  blurRadius = 8.dp,
                  shapeRadius = 8.dp,
                  offsetX = -8.dp,
                  offsetY = 8.dp
              )
              .background(Color.Green, RoundedCornerShape(8.dp)),
      )

      Box(
          modifier = Modifier
              .aspectRatio(1f)
              .fillMaxHeight()
              .padding(16.dp)
              .shadow(
                  blurRadius = 8.dp,
                  shapeRadius = 8.dp,
                  offsetX = -8.dp,
                  offsetY = -8.dp
              )
              .background(Color.Green, RoundedCornerShape(8.dp)),
      )

      Box(
          modifier = Modifier
              .aspectRatio(1f)
              .fillMaxHeight()
              .padding(16.dp)
              .shadow(
                  blurRadius = 8.dp,
                  shapeRadius = 8.dp,
              )
              .background(Color.Green, RoundedCornerShape(8.dp)),
      )
    }

    Row(
        modifier = Modifier.height(64.dp),
    ) {
      Box(
          modifier = Modifier
              .aspectRatio(1f)
              .fillMaxHeight()
              .padding(16.dp)
              .shadow(
                  blurRadius = 8.dp,
                  shapeRadius = 8.dp,
                  offsetX = -8.dp,
                  offsetY = 0.dp
              )
              .background(Color.Green, RoundedCornerShape(8.dp)),
      )

      Box(
          modifier = Modifier
              .aspectRatio(1f)
              .fillMaxHeight()
              .padding(16.dp)
              .shadow(
                  blurRadius = 8.dp,
                  shapeRadius = 8.dp,
                  offsetX = 0.dp,
                  offsetY = -8.dp
              )
              .background(Color.Green, RoundedCornerShape(8.dp)),
      )

      Box(
          modifier = Modifier
              .aspectRatio(1f)
              .fillMaxHeight()
              .padding(16.dp)
              .shadow(
                  blurRadius = 8.dp,
                  shapeRadius = 8.dp,
                  offsetX = 8.dp,
                  offsetY = 0.dp
              )
              .background(Color.Green, RoundedCornerShape(8.dp)),
      )

      Box(
          modifier = Modifier
              .aspectRatio(1f)
              .fillMaxHeight()
              .padding(16.dp)
              .shadow(
                  blurRadius = 8.dp,
                  shapeRadius = 8.dp,
                  offsetX = 0.dp,
                  offsetY = 8.dp
              )
              .background(Color.Green, RoundedCornerShape(8.dp)),
      )
    }
  }
}
