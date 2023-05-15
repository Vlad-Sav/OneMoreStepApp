package kg.android.onemorestepapp.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kg.android.onemorestepapp.models.responses.RandomStickerResponse
import kg.android.onemorestepapp.repository.RewardsRepository
import kg.android.onemorestepapp.ui.stickers.ChestOpeningUiEvent
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ChestOpeningViewModel @Inject constructor(
    private val repository: RewardsRepository
): ViewModel() {
   /* private val resultChannel = Channel<ChestOpeningResult<Unit>>()
    val chestOpeningResult = resultChannel.receiveAsFlow()*/
    val chestOpeningResponse = MutableLiveData<RandomStickerResponse?>()

    fun onEvent(event: ChestOpeningUiEvent) {
        when(event) {
            is ChestOpeningUiEvent.GetRandomSticker -> {
                getRandomSticker()
            }
        }
    }

    private fun getRandomSticker() {
        viewModelScope.launch {
            //state.value = state.value?.copy(isLoading = true)
            val result = repository.getRandomSticker()
            chestOpeningResponse.postValue(result)
            //state.value = state.value?.copy(isLoading = false)
        }
    }
}