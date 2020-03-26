package dev.anacoimbra.androidaugmentedreality.helpers

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Handler
import android.os.HandlerThread
import android.view.PixelCopy
import android.view.SurfaceView
import android.widget.Toast
import androidx.core.content.FileProvider
import dev.anacoimbra.androidaugmentedreality.R
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

fun SurfaceView.screenShot() {
    val bitmap = Bitmap.createBitmap(
        this.width,
        this.height, Bitmap.Config.ARGB_8888
    )
    val handlerThread = HandlerThread("PixelCopier")
    handlerThread.start()
    PixelCopy.request(this, bitmap, { copyResult: Int ->
        if (copyResult == PixelCopy.SUCCESS) {
            val date = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(Date())
            val filename = "AugmentedReality_$date.jpg"
            saveBitmap(context, bitmap, filename)
            openFile(context, filename)
        } else {
            Toast.makeText(this.context, R.string.error_take_screenshot, Toast.LENGTH_SHORT).show()
        }
        handlerThread.quitSafely()
    }, Handler(handlerThread.looper))
}

fun saveBitmap(context: Context, bitmap: Bitmap, filename: String) {
    context.openFileOutput(filename, Context.MODE_PRIVATE).use {
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
    }
}

fun openFile(context: Context, filename: String) {
    val file = File(context.filesDir, filename)
    val intent = Intent()
    intent.action = Intent.ACTION_SEND
    val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)

    val shareIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_STREAM, uri)
        type = "image/jpeg"
    }
    context.startActivity(
        Intent.createChooser(
            shareIntent,
            context.getString(R.string.share_with)
        )
    )
}