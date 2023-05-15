package kg.android.onemorestepapp.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kg.android.onemorestepapp.repository.RewardsRepository
import kg.android.onemorestepapp.ui.rewards.RewardsUiEvent
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RewardsViewModel @Inject constructor(
    private val repository: RewardsRepository
): ViewModel() {

    val stickersNumber = MutableLiveData<Int?>()
    var stickers = ArrayList<String?>()
    val stickersLiveData = MutableLiveData<List<String?>>()

    init {
        getStickersNumber()
    }

    fun onEvent(event: RewardsUiEvent) {
        when(event) {
            is RewardsUiEvent.GetStickers -> {
                //stickersNumber.value?.plus(1)!!
                for(i in 1..(stickersNumber.value ?: 1)){
                    getSticker(i)
                }
            }
        }
    }

    private fun getStickersNumber() {
        viewModelScope.launch {
            //state.value = state.value?.copy(isLoading = true)
            val result = repository.getStickersNumber()
            for (i in 0..(result ?: 0)) stickers.add(null)
            stickersLiveData.value = stickers
            stickersNumber.postValue(result)
            //state.value = state.value?.copy(isLoading = false)
        }
    }

    private fun getSticker(id: Int) {
        viewModelScope.launch {
            //state.value = state.value?.copy(isLoading = true)
            val result = repository.getSticker(id)
            stickers.set(id - 1, result)
            stickersLiveData.value = stickers
            //state.value = state.value?.copy(isLoading = false)
        }
    }
}