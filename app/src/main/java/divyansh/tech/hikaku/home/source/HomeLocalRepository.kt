package divyansh.tech.hikaku.home.source

import divyansh.tech.hikaku.common.Result
import divyansh.tech.hikaku.home.datamodels.PDF
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
}