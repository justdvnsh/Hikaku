package divyansh.tech.hikaku.home.callbacks

import android.widget.Toast
import timber.log.Timber
import java.io.File

class HomeCallbacks {
    fun onPdfClick(file: File) {
        Timber.e("FILE IS -> ${file.absolutePath}")
    }
}