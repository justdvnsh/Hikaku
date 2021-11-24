package divyansh.tech.hikaku.utils

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.print.PrintAttributes
import android.print.PrintManager
import android.webkit.WebView
import androidx.core.content.FileProvider
import divyansh.tech.hikaku.R
import timber.log.Timber
import java.io.File

object PdfView {

    private val REQUEST_CODE = 101

    private lateinit var pdfPrint: PdfPrint

    fun createWebPdfJob(activity: Activity, webView: WebView, directory: File, fileName: String, callback: Callback) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                activity.requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_CODE)
                callback.onFailure()
                return
            }
        }

        val jobName = activity.getString(R.string.app_name) + " Document"
        var attributes: PrintAttributes? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            attributes = PrintAttributes.Builder()
                .setMediaSize(PrintAttributes.MediaSize.ISO_A4)
                .setResolution(PrintAttributes.Resolution("pdf", "pdf", 600, 600))
                .setMinMargins(PrintAttributes.Margins.NO_MARGINS).build()
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            pdfPrint = PdfPrint(attributes!!, webView.createPrintDocumentAdapter(jobName), directory, fileName, object : PdfPrint.CallbackPrint {
                override fun success(path: String) {
                    callback.onSuccess(path)
                }

                override fun onFailure() {
                    callback.onFailure()
                }
            })
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            pdfPrint = PdfPrint(attributes!!, webView.createPrintDocumentAdapter(), directory, fileName, object : PdfPrint.CallbackPrint {
                override fun success(path: String) {
                    callback.onSuccess(path)
                }

                override fun onFailure() {
                    callback.onFailure()
                }
            })
        }

//        val manager = activity.getSystemService(Context.PRINT_SERVICE) as PrintManager
//        try{
//            manager.print("Document", pdfPrint, PrintAttributes.Builder().build())
//        } catch (e: Exception) {
//            Timber.e(e)
//        }
    }

    /**
     * create alert dialog to open the pdf file
     * @param activity pass the current activity context
     * @param title  to show the heading of the alert dialog
     * @param message  to show on the message area.
     * @param path file path create on storage directory
     */
    fun openPdfFile(activity: Activity,   path: String) {

        //check the marshmallow permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                activity.requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_CODE)
                return
            }
        }
        fileChooser(activity, path)
    }

    interface Callback {
        fun onSuccess(path: String)
        fun onFailure()
    }

    /**
     * @param activity pass the current activity context
     * @param path storage full path
     */
    private fun fileChooser(activity: Activity, path: String) {
        val file = File(path)
        val target = Intent("android.intent.action.VIEW")
        val uri = FileProvider.getUriForFile(activity, "com.package.name.fileproviders", file)
        target.setDataAndType(uri, "application/pdf")
        target.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        val intent = Intent.createChooser(target, "Open File")
        try {
            activity.startActivity(intent)
        } catch (var6: ActivityNotFoundException) {
            var6.printStackTrace()
        }

    }

}