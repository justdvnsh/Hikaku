package divyansh.tech.hikaku.home.epoxy

import androidx.databinding.ViewDataBinding
import com.airbnb.epoxy.DataBindingEpoxyModel
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import divyansh.tech.hikaku.BR
import divyansh.tech.hikaku.R
import divyansh.tech.hikaku.home.callbacks.HomeCallbacks
import java.io.File

@EpoxyModelClass(layout = R.layout.item_pdf)
abstract class EpoxyPdfModel: DataBindingEpoxyModel() {
    @EpoxyAttribute
    lateinit var pdf: File

    @EpoxyAttribute
    lateinit var callback: HomeCallbacks

    override fun setDataBindingVariables(binding: ViewDataBinding) {
        binding.setVariable(BR.file, pdf)
        binding.setVariable(BR.callback, callback)
    }
}