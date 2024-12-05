package ru.kartollika.yandexcup.canvas.compose.picker

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ru.kartollika.yandexcup.canvas.Shape
import ru.kartollika.yandexcup.canvas.mvi.EditorConfiguration
import ru.kartollika.yandexcup.components.Slider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrushSizePicker(
    editorConfiguration: EditorConfiguration,
    modifier: Modifier = Modifier,
    changeBrushSize: (Float) -> Unit = {},
    rotate: (Float) -> Unit = {},
    state: LazyListState = rememberLazyListState(),
    listAngle: List<Float> = listOf(
        -90f,
        -80f,
        -70f,
        -60f,
        -50f,
        -40f,
        -30f,
        -20f,
        -10f,
        0f,
        10f,
        20f,
        30f,
        40f,
        50f,
        60f,
        70f,
        80f,
        90f,
        100f,
        110f,
        120f,
        130f,
        140f,
        150f,
        160f,
        170f,
        180f
    )
) {
    Box(
        modifier = modifier,
    ) {

        Column(
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Slider(
                modifier = Modifier
                    .fillMaxWidth(),
                value = editorConfiguration.brushSizeByMode,
                valueRange = 4f..100f,
                onValueChange = { value ->
                    changeBrushSize(value)
                },
                invertTrack = true
            )

            LazyRow(
                contentPadding = PaddingValues(8.dp),
                modifier = Modifier,
                state = state
            ) {
                items(listAngle.size) { angle ->

                    Text(
                        modifier = Modifier
                            .size(32.dp)
                            .padding(start = 2.dp, end = 2.dp)
                            .border(width = 1.dp, color = Color.Black)
                            .clickable { rotate(listAngle[angle]) },
                        text = listAngle[angle].toString(),
                    )
                }
            }
        }
    }
}