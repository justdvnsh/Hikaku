package divyansh.tech.hikaku.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import divyansh.tech.hikaku.common.CommonViewModel
import divyansh.tech.hikaku.common.Event
import divyansh.tech.hikaku.common.Result
import divyansh.tech.hikaku.home.source.HomeDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repo: HomeDataSource
): CommonViewModel() {

    private val _pdfLiveData = MutableLiveData<ArrayList<File>>()
    val pdfLiveData get() = _pdfLiveData

    fun onFabClick() {
//        val action = HomeFragmentDirections.actionHomeFragmentToCompareFragment()
//        changeNavigation(action)
    }

    init {
        getPDF()
    }

    private fun getPDF() = viewModelScope.launch {
        val pdfs = repo.getAllPDF()
        if (pdfs is Result.Success) _pdfLiveData.postValue(pdfs.data as ArrayList<File>)
        else if (pdfs is Result.Error) Timber.e(pdfs.exception.toString())
    }
}