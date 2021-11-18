package divyansh.tech.hikaku.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import divyansh.tech.hikaku.common.CommonViewModel
import divyansh.tech.hikaku.common.Event
import divyansh.tech.hikaku.common.Result
import divyansh.tech.hikaku.home.datamodels.PDF
import divyansh.tech.hikaku.home.source.HomeDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import java.io.File
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repo: HomeDataSource
): CommonViewModel() {

    private val _pdfLiveData = MutableLiveData<ArrayList<PDF>>()
    val pdfLiveData get() = _pdfLiveData

    private val _fabLiveData = MutableLiveData<Boolean>()
    val fabLiveData get() = _fabLiveData

    private val _compareLiveData = MutableLiveData<String>()
    val compareLiveData get() = _compareLiveData

    private var localCopyOfArrayList = ArrayList<PDF>()

    fun getPDF() = viewModelScope.launch {
        val pdfs = repo.getAllPDF()
        if (pdfs is Result.Success) _pdfLiveData.postValue(pdfs.data as ArrayList<PDF>)
        else if (pdfs is Result.Error) Timber.e(pdfs.exception.toString())
    }

    fun pdfLongClick(arrayList: ArrayList<PDF>) {
        Timber.e("ARRAYLIST -> $arrayList")
        if (arrayList.size == 2) {
            _fabLiveData.value = true
            localCopyOfArrayList = arrayList
        } else _fabLiveData.value = false
        Timber.e("FAB LIVE DATA -> ${fabLiveData.value}")
    }

    fun onFabClick() = viewModelScope.launch(Dispatchers.IO) {
        val comparisonResult =
            repo.comparePDF(
                localCopyOfArrayList[0],
                localCopyOfArrayList[1]
            )

        if (comparisonResult is Result.Success)
            launch(Dispatchers.Main) {
                changeNavigation(
                    HomeFragmentDirections.actionHomeFragmentToCompareFragment(
                        comparisonResult.data
                    )
                )
            }
        else Timber.e((comparisonResult as Result.Error).exception)
    }
}