package divyansh.tech.hikaku.home.binding

import android.widget.TextView
import androidx.databinding.BindingAdapter
import divyansh.tech.hikaku.R

@BindingAdapter("selected")
fun isSelected(view: TextView, isSelected: Boolean) {
    if (isSelected) view.setBackgroundColor(view.resources.getColor(R.color.colorAccent))
    else view.setBackgroundColor(view.resources.getColor(R.color.gfgWhite))
}