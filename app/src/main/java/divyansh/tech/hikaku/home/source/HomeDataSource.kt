package divyansh.tech.hikaku.home.source

import divyansh.tech.hikaku.common.Result
import divyansh.tech.hikaku.home.datamodels.PDF
import kotlinx.coroutines.flow.Flow
import java.io.File

interface HomeDataSource {
    suspend fun getAllPDF(): Result<ArrayList<PDF>>
    suspend fun comparePDF(file1: File, file2: File): Result<String>
}