package ru.kartollika.yandexcup.canvas.frames

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.persistentListOf
import ru.kartollika.yandexcup.R
import ru.kartollika.yandexcup.canvas.compose.DrawingCanvas
import ru.kartollika.yandexcup.canvas.mvi.Frame
import ru.kartollika.yandexcup.canvas.mvi.Frames
import ru.kartollika.yandexcup.ui.theme.YandexCup2024Theme

@Composable
fun FramesScreen(
  modifier: Modifier = Modifier,
  frames: Frames,
  selectFrame: (Int) -> Unit = {},
  deleteFrame: (Int) -> Unit = {},
  deleteAllFrames: () -> Unit = {},
) {
  Surface(
    modifier = modifier
  ) {
    Column {
      TextButton(
        modifier = Modifier
          .padding(end = 16.dp)
          .align(Alignment.End),
        onClick = {
          deleteAllFrames()
        }
      ) {
        Text("Удалить кадры", color = Color.Red)
      }

      LazyColumn(
        modifier = Modifier.fillMaxSize()
      ) {
        itemsIndexed(frames) { index, frame ->
          FrameItem(
            modifier = Modifier
              .fillMaxWidth()
              .clickable {
                selectFrame(index)
              }
              .padding(16.dp),
            frame = frame,
            index = index,
            deleteFrame = {
              deleteFrame(index)
            }
          )
        }
      }
    }
  }
}

@Composable
fun FrameItem(
  modifier: Modifier = Modifier,
  frame: Frame,
  index: Int,
  deleteFrame: () -> Unit = {},
) {
  Row(
    modifier = modifier,
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    DrawingCanvas(
      paths = {
        frame.paths
      },
      currentPath = {
        null
      },
      previousPaths = {
        null
      },
      modifier = Modifier
        .height(100.dp)
        .width(60.dp)
        .background(Color.White),
      // TODO Вычислить программно этот скейл
      scale = 0.16f
    )

    Text(
      text = "Кадр $index",
      modifier = Modifier.weight(1f),
    )

    IconButton(
      onClick = {
        deleteFrame()
      }
    ) {
      Icon(
        modifier = Modifier,
        painter = painterResource(R.drawable.bin),
        contentDescription = null
      )
    }
  }
}

@Preview
@Composable
private fun FramesScreenPreview() {
  YandexCup2024Theme {
    FramesScreen(
      modifier = Modifier.fillMaxSize(),
      frames = persistentListOf(Frame())
    )
  }
}