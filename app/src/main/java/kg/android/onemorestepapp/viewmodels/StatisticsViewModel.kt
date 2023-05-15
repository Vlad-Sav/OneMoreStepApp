package kg.android.onemorestepapp.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kg.android.onemorestepapp.models.responses.TopUsersResponse
import kg.android.onemorestepapp.models.responses.UserProfileResponse
import kg.android.onemorestepapp.models.responses.UsersPinnedStickerResponse
import kg.android.onemorestepapp.repository.ProfileRepository
import kg.android.onemorestepapp.repository.StatisticsRepository
import kg.android.onemorestepapp.ui.profile.ProfileUiEvent
import kg.android.onemorestepapp.ui.statistics.StatisticsUiEvent
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModelProfileViewModel @Inject constructor(
    private val repository: StatisticsRepository
): ViewModel() {
    //private val resultChannel = Channel<ProfileResult<Unit>>()
    //val profileResult = resultChannel.receiveAsFlow()
    val topUsersResponse = MutableLiveData<List<TopUsersResponse>?>()

    init {
        getTopUsers()
    }

    fun onEvent(event: StatisticsUiEvent) {
        when(event) {
            is StatisticsUiEvent.GetTopUsers -> {
                getTopUsers()
            }
        }
    }

    private fun getTopUsers() {
        viewModelScope.launch {
            //state.value = state.value?.copy(isLoading = true)
            val result = repository.getTopUsers()
            topUsersResponse.postValue(result)
            //state.value = state.value?.copy(isLoading = false)
        }
    }
}