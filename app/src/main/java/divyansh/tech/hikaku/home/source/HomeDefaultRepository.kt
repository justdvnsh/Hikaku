package divyansh.tech.hikaku.home.source

import android.os.Environment
import divyansh.tech.hikaku.common.Result
import divyansh.tech.hikaku.home.datamodels.PDF
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import java.lang.Exception
import javax.inject.Inject

class HomeDefaultRepository @Inject constructor(
    private val localRepo: HomeLocalRepository
): HomeDataSource {
    override suspend fun getAllPDF(): Result<ArrayList<PDF>> {
        val pdfs = localRepo.getAllPDF(Environment.getExternalStorageDirectory())
        return if (pdfs.isNullOrEmpty()) Result.Error(Exception("Could not fetch pdfs"))
        else Result.Success(pdfs)
    }
}