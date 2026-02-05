package ec.edu.uce.final_amsilvac1.util

import android.content.Context
import android.graphics.Bitmap
import java.io.File
import java.io.FileOutputStream
import java.util.*

object FileUtils {
    fun saveBitmap(context: Context, bitmap: Bitmap, prefix: String = "prod_"): String {
        val filename = "$prefix${UUID.randomUUID()}.png"
        val file = File(context.filesDir, filename)
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out)
            out.flush()
        }
        return file.absolutePath
    }
}
