package divyansh.tech.hikaku.utils

import android.os.Build
import android.os.Bundle
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.print.PageRange
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintDocumentInfo
import android.util.Log
import java.io.File

class PdfPrint(
    private val printAttributes: PrintAttributes,
    private val printAdapter: PrintDocumentAdapter,
    private val path: File,
    private val fileName: String,
    private val callback: CallbackPrint
): PrintDocumentAdapter() {

//    fun print(printAdapter: PrintDocumentAdapter, path: File, fileName: String, callback: CallbackPrint) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            printAdapter.onLayout(null, printAttributes, null, object : PrintDocumentAdapter.LayoutResultCallback() {
//                override fun onLayoutFinished(info: PrintDocumentInfo, changed: Boolean) {
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                        printAdapter.onWrite(arrayOf(PageRange.ALL_PAGES), getOutputFile(path, fileName), CancellationSignal(), object : PrintDocumentAdapter.WriteResultCallback() {
//                            override fun onWriteFinished(pages: Array<PageRange>) {
//
//
//                            }
//                        })
//                    }
//                }
//            }, null)
//        }
//    }
//
    private fun getOutputFile(path: File, fileName: String): ParcelFileDescriptor? {
        if (!path.exists()) {
            path.mkdirs()
        }
        val file = File(path, fileName)
        try {
            file.createNewFile()
            return ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_WRITE)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to open ParcelFileDescriptor", e)
        }

        return null
    }


    interface CallbackPrint {
        fun success(path: String)
        fun onFailure()
    }

    companion object {
        private val TAG = PdfPrint::class.java.simpleName
    }

    override fun onLayout(
        p0: PrintAttributes?,
        printAttributes1: PrintAttributes?,
        cancellationSignal: CancellationSignal?,
        layoutResultCallback: LayoutResultCallback?,
        bundle: Bundle?
    ) {
        cancellationSignal?.let {
            if (it.isCanceled) {
                layoutResultCallback?.onLayoutCancelled()
            } else {
                val builder = PrintDocumentInfo.Builder(" file name")
                builder.setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                    .setPageCount(PrintDocumentInfo.PAGE_COUNT_UNKNOWN)
                    .build()
                layoutResultCallback?.onLayoutFinished(
                    builder.build(),
                    !printAttributes1?.equals(printAttributes)!!
                )
            }
        }
    }

    override fun onWrite(
        pageRange: Array<out PageRange>?,
        parcelFileDescriptor:  ParcelFileDescriptor?,
        cancellationSignal: CancellationSignal?,
        writeResultCallback: WriteResultCallback?
    ) {
        pageRange?.let {
            if (it.isNotEmpty()) {
                val file = File(path, fileName)
                val path = file.absolutePath
                callback.success(path)
            } else callback.onFailure()
        }
        writeResultCallback?.onWriteFinished(pageRange)
    }
}