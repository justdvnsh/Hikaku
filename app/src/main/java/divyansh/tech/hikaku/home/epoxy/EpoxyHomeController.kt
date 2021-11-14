package divyansh.tech.hikaku.home.epoxy

import com.airbnb.epoxy.TypedEpoxyController
import divyansh.tech.hikaku.home.callbacks.HomeCallbacks
import java.io.File

class EpoxyHomeController(
    private val callbacks: HomeCallbacks
): TypedEpoxyController<ArrayList<File>>() {
    override fun buildModels(data: ArrayList<File>?) {
        data?.let {
            it.forEach {
                epoxyPdf {
                    id(it.hashCode())
                    pdf(it)
                    callback(callbacks)
                }
            }
        }
    }
}