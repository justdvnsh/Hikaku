package divyansh.tech.hikaku.home.source

import com.itextpdf.text.pdf.PdfReader
import com.itextpdf.text.pdf.parser.PdfTextExtractor
import divyansh.tech.hikaku.common.Result
import divyansh.tech.hikaku.home.datamodels.PDF
import divyansh.tech.hikaku.utils.diff_match_patch
import timber.log.Timber
import java.io.File
import javax.inject.Inject

class HomeLocalRepository @Inject constructor() {

    suspend fun getAllPDF(file: File): ArrayList<PDF> {
        val result = arrayListOf<PDF>()
        file.listFiles()?.forEach {
            if (it.isDirectory && !it.isHidden) result.addAll(getAllPDF(it))
            else if (it.name.endsWith(".pdf")) result.add(PDF(
                file = it,
                isSelected = false
            ))
        }
        return result
    }

    suspend fun getTextFromPDF(filePath: String): Result<String> {
        return try {
            var parsedText = ""
            val reader = PdfReader(filePath)
            val n: Int = reader.numberOfPages
            for (i in 0 until n) {
                parsedText += PdfTextExtractor.getTextFromPage(reader, i + 1).trim() + "\n"
            }
            Timber.e("TEXT FROM DOC-> $parsedText")
            reader.close()
            Result.Success(parsedText)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun comparePDF(text1: String, text2: String): String {
        val diff = diff_match_patch()
        diff.Diff_Timeout = 1F
        diff.Diff_EditCost = 4
        val d = diff.diff_main(text1, text2)
        Timber.e("PRETTY HTML -> ${diff.diff_prettyHtml(d)}")
        return diff.diff_prettyHtml(d).replace("&para;", "")
    }
}