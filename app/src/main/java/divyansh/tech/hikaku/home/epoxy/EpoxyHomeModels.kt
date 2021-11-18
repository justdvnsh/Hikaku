package divyansh.tech.hikaku.home.epoxy

import android.graphics.Color
import android.view.View
import android.widget.TextView
import androidx.databinding.ViewDataBinding
import com.airbnb.epoxy.*
import divyansh.tech.hikaku.BR
import divyansh.tech.hikaku.R
import divyansh.tech.hikaku.home.callbacks.HomeCallbacks
import divyansh.tech.hikaku.home.datamodels.PDF
import timber.log.Timber
import java.io.File

//@EpoxyModelClass(layout = R.layout.item_pdf)
//abstract class EpoxyPdfModel: DataBindingEpoxyModel() {
//    @EpoxyAttribute
//    lateinit var pdf: File
//
//    @EpoxyAttribute
//    lateinit var callback: HomeCallbacks
//
//    override fun setDataBindingVariables(binding: ViewDataBinding) {
//        binding.setVariable(BR.file, pdf)
//        binding.setVariable(BR.callback, callback)
//    }
//}

private val selectedItemList = ArrayList<PDF>()

@EpoxyModelClass(layout = R.layout.item_pdf)
abstract class EpoxyPdfModel: EpoxyModelWithHolder<EpoxyPdfModel.Holder>() {

    @EpoxyAttribute
    lateinit var file: PDF

    @EpoxyAttribute
    lateinit var callbacks: HomeCallbacks

    override fun bind(holder: Holder) {
        super.bind(holder)
        holder.fileName.apply {
            text = file.file.name
            setOnClickListener {
                callbacks.onPdfClick(file.file)
            }
            setOnLongClickListener {
                file.isSelected = !file.isSelected
                if (file.isSelected) {
                    this.setBackgroundColor(this.resources.getColor(R.color.colorAccent))
                    selectedItemList.add(file)
                }
                else {
                    this.setBackgroundColor(this.resources.getColor(R.color.gfgWhite))
                    selectedItemList.remove(file)
                }
                callbacks.onPdfLongClick(selectedItemList)
                true
            }
        }
    }

    class Holder: EpoxyHolder() {
        lateinit var fileName: TextView

        override fun bindView(itemView: View) {
            fileName = itemView.findViewById(R.id.fileName)
        }

    }
}