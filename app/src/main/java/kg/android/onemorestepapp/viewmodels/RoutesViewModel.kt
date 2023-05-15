package kg.android.onemorestepapp.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kg.android.onemorestepapp.models.RoutesResult
import kg.android.onemorestepapp.models.responses.RoutesResponse
import kg.android.onemorestepapp.repository.RoutesRepository
import kg.android.onemorestepapp.ui.routes.RouteFetchingState
import kg.android.onemorestepapp.ui.routes.RoutesUiEvent
import kg.android.onemorestepapp.utils.AuthState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoutesViewModel @Inject constructor(
    private val repository: RoutesRepository
): ViewModel() {
    var state = MutableLiveData<RouteFetchingState>()

    private val resultChannel = Channel<RoutesResult<Unit>>()
    val routesResult = resultChannel.receiveAsFlow()

    val routes = MutableLiveData<List<RoutesResponse>?>()

    init {
        getRoutes()
    }

    fun onEvent(event: RoutesUiEvent) {
        when(event) {
            is RoutesUiEvent.GetRoutes -> {
                getRoutes()
            }
        }
    }

    private fun getRoutes() {
        viewModelScope.launch {
            state.value = state.value?.copy(isLoading = true)
            val result = repository.getRoutes()
            routes.postValue(result)
            if(result != null)
                resultChannel.send(RoutesResult.ProfileNotFetched())
            state.value = state.value?.copy(isLoading = false)
        }
    }
}