package divyansh.tech.hikaku.home.callbacks

import android.widget.Toast
import divyansh.tech.hikaku.home.HomeFragmentDirections
import divyansh.tech.hikaku.home.HomeViewModel
import timber.log.Timber
import java.io.File

class HomeCallbacks(
    private val viewModel: HomeViewModel
) {
    fun onPdfClick(file: File) {
        val action = HomeFragmentDirections.actionHomeFragmentToCompareFragment(file.absolutePath)
        viewModel.changeNavigation(action)
    }
}