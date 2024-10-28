package ru.kartollika.yandexcup.canvas

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.kartollika.yandexcup.R
import ru.kartollika.yandexcup.ui.theme.YandexCup2024Theme

@Composable
fun CanvasScreen(
  modifier: Modifier = Modifier,
) {
  Surface(
    modifier = modifier,
  ) {
    Column(
      modifier = Modifier.fillMaxSize(),
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      TopControls(
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 16.dp)
          .padding(horizontal = 16.dp)
          .statusBarsPadding(),
      )

      val canvasBackground = ImageBitmap.imageResource(R.drawable.canvas)
      DrawingCanvas(
        modifier = Modifier
          .weight(1f)
          .fillMaxWidth()
          .padding(16.dp)
          .clip(RoundedCornerShape(32.dp))
          .drawBehind {
            drawImage(canvasBackground)
          }
      )

      BottomControls(
        modifier = Modifier
          .fillMaxWidth()
          .navigationBarsPadding()
          .padding(horizontal = 16.dp)
          .padding(bottom = 16.dp),
      )
    }
  }
}

@Composable fun TopControls(
  modifier: Modifier = Modifier,
) {
  Row(
    modifier = modifier,
    horizontalArrangement = Arrangement.SpaceBetween,
  ) {
    Row {
      Icon(
        modifier = Modifier.size(32.dp),
        painter = painterResource(R.drawable.undo),
        tint = Color.White,
        contentDescription = null
      )

      Icon(
        modifier = Modifier.size(32.dp),
        painter = painterResource(R.drawable.redo),
        tint = Color.White,
        contentDescription = null
      )
    }

    Row {
      Icon(
        modifier = Modifier.size(32.dp),
        painter = painterResource(R.drawable.bin),
        tint = Color.White,
        contentDescription = null
      )

      Icon(
        modifier = Modifier.size(32.dp),
        painter = painterResource(R.drawable.file_plus),
        tint = Color.White,
        contentDescription = null
      )

      Icon(
        modifier = Modifier.size(32.dp),
        painter = painterResource(R.drawable.layers),
        tint = Color.White,
        contentDescription = null
      )
    }

    Row {
      Icon(
        modifier = Modifier.size(32.dp),
        painter = painterResource(R.drawable.pause),
        tint = Color.White,
        contentDescription = null
      )

      Icon(
        modifier = Modifier.size(32.dp),
        painter = painterResource(R.drawable.play),
        tint = Color.White,
        contentDescription = null
      )
    }
  }
}

@Composable fun DrawingCanvas(
  modifier: Modifier = Modifier,
) {
  Canvas(
    modifier = modifier,
  ) {

  }
}

@Composable fun BottomControls(
  modifier: Modifier = Modifier,
) {
  Row(
    modifier = modifier,
    horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
  ) {
    Icon(
      modifier = Modifier.size(32.dp),
      painter = painterResource(R.drawable.pencil),
      tint = Color.White,
      contentDescription = null,
    )

    Icon(
      modifier = Modifier.size(32.dp),
      painter = painterResource(R.drawable.brush),
      contentDescription = null,
      tint = Color.White
    )

    Icon(
      modifier = Modifier.size(32.dp),
      painter = painterResource(R.drawable.erase),
      contentDescription = null,
      tint = Color.White
    )

    Icon(
      modifier = Modifier.size(32.dp),
      painter = painterResource(R.drawable.instruments),
      contentDescription = null,
      tint = Color.White
    )

    Spacer(
      modifier = Modifier
        .size(32.dp)
        .background(Color.Blue, CircleShape),
    )
  }
}

@Preview
@Composable
private fun CanvasScreenPreview() {
  YandexCup2024Theme {
    CanvasScreen(
      modifier = Modifier.fillMaxSize(),
    )
  }
}