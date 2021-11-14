package divyansh.tech.hikaku.home.source

import divyansh.tech.hikaku.common.Result
import java.io.File
import javax.inject.Inject

class HomeLocalRepository @Inject constructor() {

    suspend fun getAllPDF(file: File): ArrayList<File> {
        val result = arrayListOf<File>()
        file.listFiles()?.forEach {
            if (it.isDirectory && !it.isHidden) result.addAll(getAllPDF(it))
            else if (it.name.endsWith(".pdf")) result.add(it)
        }
        return result
    }
}