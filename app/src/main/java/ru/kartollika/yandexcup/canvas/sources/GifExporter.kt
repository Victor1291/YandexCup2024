package ru.kartollika.yandexcup.canvas.sources

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.Style.STROKE
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.IntSize
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.kartollika.yandexcup.canvas.mvi.Frames
import ru.kartollika.yandexcup.core.AnimatedGifEncoder
import java.io.File
import java.io.OutputStream
import javax.inject.Inject

class GifExporter @Inject constructor(
  @ApplicationContext
  private val context: Context,
) {
  suspend fun export(
    frames: Frames,
    canvasSize: IntSize,
    fileName: String = "gif",
  ) =
    withContext(Dispatchers.IO + CoroutineExceptionHandler { coroutineContext, throwable ->
      println(throwable)
    }) {
      val dir = context.getDir(fileName, Context.MODE_PRIVATE)
      val file = File(dir, fileName)
      val outputStream: OutputStream = file.outputStream()

      val gifEncoder = AnimatedGifEncoder()
      gifEncoder.start(outputStream)

      withContext(Dispatchers.Default) {

        frames.forEach { frame ->
          val bitmap = Bitmap.createBitmap(canvasSize.width, canvasSize.height, Bitmap.Config.ARGB_8888)
          val canvas = Canvas(bitmap)

          val save = canvas.saveLayer(null, null)
          frame.paths.forEach { pathWithProperties ->
            canvas.drawPath(
              pathWithProperties.path.asAndroidPath(),
              Paint().apply {
                strokeWidth = pathWithProperties.properties.brushSize
                style = STROKE
                color = if (pathWithProperties.properties.eraseMode) {
                  android.graphics.Color.TRANSPARENT
                } else {
                  pathWithProperties.properties.color.toArgb()
                }
                isAntiAlias = true
                xfermode = if (pathWithProperties.properties.eraseMode) {
                  PorterDuffXfermode(PorterDuff.Mode.CLEAR)
                } else {
                  null
                }
              })
          }
          canvas.restoreToCount(save)

          gifEncoder.addFrame(bitmap)
        }
        gifEncoder.finish()
      }
      outputStream.close()
    }
}