package divyansh.tech.hikaku.home.screens.compare

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import divyansh.tech.hikaku.common.CommonViewModel
import divyansh.tech.hikaku.common.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompareViewModel @Inject constructor(): CommonViewModel() {

    private val _buttonClickLiveData = MutableLiveData<Event<BUTTONS>>()
    val buttonClickLiveData get() = _buttonClickLiveData

    enum class BUTTONS {
        FIRST, SECOND
    }

    fun onClick(button: BUTTONS) {
        viewModelScope.launch(Dispatchers.IO) {
            _buttonClickLiveData.postValue(Event(button))
        }
    }
}