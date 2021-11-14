package divyansh.tech.hikaku.home.source

import divyansh.tech.hikaku.common.Result
import kotlinx.coroutines.flow.Flow
import java.io.File

interface HomeDataSource {
    suspend fun getAllPDF(): Result<ArrayList<File>>
}