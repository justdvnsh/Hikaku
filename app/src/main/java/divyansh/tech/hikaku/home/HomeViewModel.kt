package divyansh.tech.hikaku.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import divyansh.tech.hikaku.common.CommonViewModel
import divyansh.tech.hikaku.common.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(): CommonViewModel() {

    fun onFabClick() {
        val action = HomeFragmentDirections.actionHomeFragmentToCompareFragment()
        changeNavigation(action)
    }
}