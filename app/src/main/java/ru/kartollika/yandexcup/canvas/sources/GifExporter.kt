package ru.kartollika.yandexcup.canvas.sources

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.Cap.ROUND
import android.graphics.Paint.Join
import android.graphics.Paint.Style.STROKE
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.IntSize
import androidx.core.graphics.withScale
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.kartollika.yandexcup.R
import ru.kartollika.yandexcup.canvas.mvi.Frame
import ru.kartollika.yandexcup.canvas.mvi.RealFrame
import ru.kartollika.yandexcup.core.AnimatedGifEncoder
import java.io.File
import java.io.OutputStream
import javax.inject.Inject

class GifExporter @Inject constructor(
  @ApplicationContext
  private val context: Context,
) {
  /**
   * @param scaleValue The more value the less quality will be for gif
   */
  suspend fun export(
    frames: List<Frame>,
    canvasSize: IntSize,
    delay: Int = 0,
    fileName: String = "gif",
    scaleValue: Int = 3,
    onProgress: (Int) -> Unit = {},
  ): File =
    withContext(
      CoroutineExceptionHandler { _, throwable ->
        println(throwable)
      }
    ) {
      val dir = context.filesDir
      val file = File(dir, fileName)
      val outputStream: OutputStream = file.outputStream()

      val gifEncoder = AnimatedGifEncoder()
      gifEncoder.start(outputStream)
      gifEncoder.setQuality(2)
      gifEncoder.setDelay(delay)
      val backgroundBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.canvas)

      val paint = Paint().apply {
        style = STROKE
        strokeCap = ROUND
        strokeJoin = Join.ROUND
        isAntiAlias = true
      }
      var processed = 0

      withContext(Dispatchers.Default) {
        frames.forEach { frame ->
          val materializedFrame = frame.materialize() as RealFrame

          val bitmap =
            Bitmap.createBitmap(
              canvasSize.width / scaleValue,
              canvasSize.height / scaleValue,
              Bitmap.Config.RGB_565
            )
          val canvas = Canvas(bitmap)

          canvas.drawBitmap(
            backgroundBitmap,
            null,
            Rect(
              0,
              0,
              canvasSize.width / scaleValue,
              canvasSize.height / scaleValue
            ),
            null
          )

          val save = canvas.saveLayer(null, null)
          materializedFrame.paths.forEach { pathWithProperties ->
            canvas.withScale(x = 1f / scaleValue, y = 1f / scaleValue) {
              canvas.drawPath(
                pathWithProperties.path.asAndroidPath(),
                paint.apply {
                  strokeWidth = pathWithProperties.properties.brushSize
                  color = if (pathWithProperties.properties.eraseMode) {
                    android.graphics.Color.TRANSPARENT
                  } else {
                    pathWithProperties.properties.color.toArgb()
                  }
                  xfermode = if (pathWithProperties.properties.eraseMode) {
                    PorterDuffXfermode(PorterDuff.Mode.CLEAR)
                  } else {
                    null
                  }
                }
              )
            }
          }
          canvas.restoreToCount(save)
          gifEncoder.addFrame(bitmap)
          onProgress(++processed)
        }
      }

      gifEncoder.finish()
      outputStream.close()
      file
    }
}