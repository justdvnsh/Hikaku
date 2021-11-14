package divyansh.tech.hikaku.home.epoxy

import com.airbnb.epoxy.TypedEpoxyController
import divyansh.tech.hikaku.home.callbacks.HomeCallbacks
import divyansh.tech.hikaku.home.datamodels.PDF
import java.io.File

class EpoxyHomeController(
    private val callbacks: HomeCallbacks
): TypedEpoxyController<ArrayList<PDF>>() {
    override fun buildModels(data: ArrayList<PDF>?) {
        data?.let {
            it.forEach {
                epoxyPdf {
                    id(it.hashCode())
                    file(it)
                    callbacks(callbacks)
                }
            }
        }
    }
}