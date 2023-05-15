package kg.android.onemorestepapp.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kg.android.onemorestepapp.models.AuthResult
import kg.android.onemorestepapp.models.ProfileResult
import kg.android.onemorestepapp.models.responses.UserProfileResponse
import kg.android.onemorestepapp.models.responses.UsersPinnedStickerResponse
import kg.android.onemorestepapp.repository.AuthRepository
import kg.android.onemorestepapp.repository.ProfileRepository
import kg.android.onemorestepapp.ui.auth.AuthUiEvent
import kg.android.onemorestepapp.ui.profile.ProfileUiEvent
import kg.android.onemorestepapp.utils.AuthState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: ProfileRepository
): ViewModel() {
    //private val resultChannel = Channel<ProfileResult<Unit>>()
    //val profileResult = resultChannel.receiveAsFlow()
    val userProfile = MutableLiveData<UserProfileResponse?>()
    val usersPinnedSticker = MutableLiveData<UsersPinnedStickerResponse?>()

    init {
        getUserProfile()
        getUsersPinnedSticker()
    }

    fun onEvent(event: ProfileUiEvent) {
        when(event) {
            is ProfileUiEvent.GetUserProfile -> {
                getUserProfile()
            }
        }
    }

    private fun getUserProfile() {
        viewModelScope.launch {
            //state.value = state.value?.copy(isLoading = true)
            val result = repository.getUserProfile()
            userProfile.postValue(result)
            //state.value = state.value?.copy(isLoading = false)
        }
    }

    private fun getUsersPinnedSticker() {
        viewModelScope.launch {
            //state.value = state.value?.copy(isLoading = true)
            val result = repository.getUsersPinnedSticker()
            usersPinnedSticker.postValue(result)
            //state.value = state.value?.copy(isLoading = false)
        }
    }

    fun logout(){
        viewModelScope.launch {
            repository.logout()
        }
    }
}