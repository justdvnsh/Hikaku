package divyansh.tech.hikaku.home.source

import android.os.Environment
import divyansh.tech.hikaku.common.Result
import divyansh.tech.hikaku.home.datamodels.PDF
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import javax.inject.Inject
import kotlin.Exception

class HomeDefaultRepository @Inject constructor(
    private val localRepo: HomeLocalRepository
): HomeDataSource {
    override suspend fun getAllPDF(): Result<ArrayList<PDF>> {
        val pdfs = localRepo.getAllPDF(Environment.getExternalStorageDirectory())
        return if (pdfs.isNullOrEmpty()) Result.Error(Exception("Could not fetch pdfs"))
        else Result.Success(pdfs)
    }

    override suspend fun comparePDF(file1: PDF, file2: PDF): Result<String> {
        val text1 = localRepo.getTextFromPDF(file1.file.absolutePath)
        val text2 = localRepo.getTextFromPDF(file2.file.absolutePath)
        return if (text1 is Result.Success && text2 is Result.Success)
            Result.Success(localRepo.comparePDF(text1.data, text2.data))
        else Result.Error(Exception("SOMETHING HAPPPENED"))
    }
}