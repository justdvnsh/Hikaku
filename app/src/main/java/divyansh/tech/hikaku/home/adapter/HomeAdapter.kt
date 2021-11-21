package divyansh.tech.hikaku.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import divyansh.tech.hikaku.R
import divyansh.tech.hikaku.databinding.ItemPdfBinding
import divyansh.tech.hikaku.home.datamodels.PDF
import java.io.File

class HomeAdapter(
    private val listener: OnClickListener
): RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    private var _list: ArrayList<PDF> = ArrayList()
    private var counter = 0
    private val selectedList = ArrayList<File>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeAdapter.ViewHolder {
        return ViewHolder(
            ItemPdfBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: HomeAdapter.ViewHolder, position: Int) {
        holder.bind(_list[position])
    }

    override fun getItemCount(): Int = _list.size

//    fun getCounterValue(): Int = counter

    fun setItems(list: ArrayList<PDF>) {
        _list = list
    }

    inner class ViewHolder(private val binding: ItemPdfBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PDF) {
            binding.fileName.text = item.file.name
            binding.fileName.setOnClickListener {
                listener.onClick(item)
            }
            binding.fileName.setOnLongClickListener {
                item.isSelected = !item.isSelected
                if (item.isSelected) {
                    binding.fileName.setBackgroundColor(binding.fileName.resources.getColor(R.color.colorAccent))
                    increaseCounter()
                    if (!selectedList.contains(item.file)) selectedList.add(item.file)
                } else {
                    binding.fileName.setBackgroundColor(binding.fileName.resources.getColor(R.color.gfgWhite))
                    decreaseCounter()
                    if (selectedList.contains(item.file)) selectedList.remove(item.file)
                }
                listener.onLongClick(selectedList)
                true
            }
        }
    }

    private fun increaseCounter() {
        if (counter <= 2) counter++
    }

    private fun decreaseCounter() {
        if (counter < 0) counter = 0
        else counter--
    }

    interface OnClickListener {
        fun onClick(item: PDF)
        fun onLongClick(list: ArrayList<File>)
    }
}