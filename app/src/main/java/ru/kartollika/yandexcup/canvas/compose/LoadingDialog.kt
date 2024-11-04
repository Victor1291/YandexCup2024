package ru.kartollika.yandexcup.canvas.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import ru.kartollika.yandexcup.R

@Composable
fun LoadingDialog(modifier: Modifier = Modifier) {
  Dialog(
    onDismissRequest = {

    },
    properties = DialogProperties(
      dismissOnBackPress = false,
      dismissOnClickOutside = false,
    )
  ) {
    Surface(
      tonalElevation = 6.dp,
      shape = RoundedCornerShape(28.dp)
    ) {
      Row(
        modifier = modifier
          .padding(horizontal = 32.dp, vertical = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        CircularProgressIndicator()

        Text(
          text = stringResource(R.string.loading_in_progress),
        )
      }
    }
  }
}