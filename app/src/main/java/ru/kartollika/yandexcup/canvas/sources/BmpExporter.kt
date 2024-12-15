package ru.kartollika.yandexcup.canvas.sources

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION
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
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.IntSize
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.graphics.withScale
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.kartollika.yandexcup.R
import ru.kartollika.yandexcup.canvas.mvi.Frame
import ru.kartollika.yandexcup.canvas.mvi.RealFrame
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import javax.inject.Inject

class BmpExporter @Inject constructor(
    @ApplicationContext
    private val context: Context,
) {


    fun shareBitmap( frames: List<Frame>,
                     canvasSize: IntSize,
                     scaleValue: Int = 3,
                     fileName: String) {
        val bitmap = getBitmap(frames[0], canvasSize, scaleValue)

        val bmpWithBackground =
            Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bmpWithBackground)
        //canvas.drawColor(Color.White)
        canvas.drawBitmap(bitmap, 0f, 0f, null)

        val cachePath = File(context.cacheDir, "images")
        Log.i("cachePath", cachePath.toString())
        cachePath.mkdirs()

        try {
            val stream =
                FileOutputStream("$cachePath/share.png")
            bmpWithBackground.compress(
                Bitmap.CompressFormat.PNG,
                100,
                stream
            )
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        val bitmapFile = File(cachePath, "share.png")
        val contentUri = FileProvider.getUriForFile(
            context,
            "com.olup.notable.provider", //(use your app signature + ".provider" )
            bitmapFile
        );

        val sendIntent = Intent().apply {
            if (contentUri != null) {
                action = Intent.ACTION_SEND
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) // temp permission for receiving app to read this file
                putExtra(Intent.EXTRA_STREAM, contentUri);
                type = "image/png";
            }

            context.grantUriPermission("android", contentUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }

        ContextCompat.startActivity(context, Intent.createChooser(sendIntent, "Choose an app"), null)
    }

    /**
     * bitmap保存为一个文件
     * @param bitmap bitmap对象
     * @return 文件对象
     */

    suspend fun saveBitmapFile(
        frames: List<Frame>,
        canvasSize: IntSize,
        scaleValue: Int = 3,
        fileName: String
    ): File = withContext(
        CoroutineExceptionHandler { _, throwable ->
            println(throwable)
        }
    ) {
        val filePath = context.filesDir
        val file = File(filePath,"$filePath.jpg")
        val bitmap = getBitmap(frames[0], canvasSize, scaleValue)
        try {
            val outputStream = BufferedOutputStream(FileOutputStream(file))
            bitmap.compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
            outputStream.flush()
            outputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val contentResolver = context.contentResolver
        val values = ContentValues(4)
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        values.put(MediaStore.Images.Media.ORIENTATION, 0)
        values.put(MediaStore.Images.Media.TITLE, "scp_donation")
        values.put(MediaStore.Images.Media.DESCRIPTION, "scp_donation")
        values.put(MediaStore.Images.Media.DATA, file.absolutePath)
        values.put(MediaStore.Images.Media.DATE_MODIFIED, System.currentTimeMillis() / 1000)
        var url: Uri? = null

        try {
            context.grantUriPermission(
                context.packageName,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, FLAG_GRANT_WRITE_URI_PERMISSION
            )
            url = FileProvider.getUriForFile(
                context,
                context.applicationContext.packageName + ".provider",
                file
            ) // contentResolver?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            //其实质是返回 Image.Media.DATA中图片路径path的转变而成的uri
            url?.let {
                val imageOut = contentResolver?.openOutputStream(url)
                imageOut?.use {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, imageOut)
                }
                val id = ContentUris.parseId(url)
                MediaStore.Images.Thumbnails.getThumbnail(
                    contentResolver, id, MediaStore.Images.Thumbnails.MINI_KIND,
                    null
                )
            }

        } catch (e: Exception) {
            if (url != null) {
                contentResolver?.delete(url, null, null)
            }
        }
       // shareGif(context,file)
        file
    }


    fun shareGif(context: Context, file: File) {
        val intentShareFile = Intent(Intent.ACTION_SEND)
        if (file.exists()) {
            intentShareFile.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intentShareFile.setType("file/*")
            intentShareFile.putExtra(
                Intent.EXTRA_STREAM,
                FileProvider.getUriForFile(
                    context,
                    context.applicationContext.packageName + ".provider",
                    file
                )
            )

            intentShareFile.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.share_gif_extra_message))
            context.startActivity(
                Intent.createChooser(
                    intentShareFile,
                    context.getString(R.string.share_gif_chooser_title)
                )
            )
        }
    }

    /**
     * @param scaleValue The more value the less quality will be for gif
     */
    suspend fun export(
        frames: List<Frame>,
        canvasSize: IntSize,
        delay: Int = 0,
        fileName: String = "bmp",
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

            //val gifEncoder = AnimatedGifEncoder()
            // gifEncoder.start(outputStream)
            // gifEncoder.setQuality(2)
            // gifEncoder.setDelay(delay)
            val backgroundBitmap =
                BitmapFactory.decodeResource(context.resources, R.drawable.canvas)

            val paint = Paint().apply {
                style = STROKE
                strokeCap = ROUND
                strokeJoin = Join.ROUND
                isAntiAlias = true
            }
            var processed = 0

            withContext(Dispatchers.Default) {
                frames[0].also { frame ->
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
                    //    gifEncoder.addFrame(bitmap)
                    onProgress(++processed)
                }
            }

            //  gifEncoder.finish()
            outputStream.close()
            file
        }


    fun getBitmap(
        frame: Frame,
        canvasSize: IntSize,
        scaleValue: Int = 3,
    ): Bitmap {
        val backgroundBitmap =
            BitmapFactory.decodeResource(context.resources, R.drawable.canvas)

        val paint = Paint().apply {
            style = STROKE
            strokeCap = ROUND
            strokeJoin = Join.ROUND
            isAntiAlias = true
        }
        var processed = 0

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
        return bitmap
    }
}