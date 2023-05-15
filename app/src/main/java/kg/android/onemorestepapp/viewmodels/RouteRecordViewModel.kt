package kg.android.onemorestepapp.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kg.android.onemorestepapp.models.AuthResult
import kg.android.onemorestepapp.repository.ProfileRepository
import kg.android.onemorestepapp.repository.RouteRecordRepository
import kg.android.onemorestepapp.ui.auth.AuthUiEvent
import kg.android.onemorestepapp.ui.routerecording.RouteSaveResult
import kg.android.onemorestepapp.ui.routerecording.RouteSaveState
import kg.android.onemorestepapp.ui.routerecording.RouteSaveUiEvent
import kg.android.onemorestepapp.utils.AuthState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class RouteRecordViewModel @Inject constructor(
    private val repository: RouteRecordRepository
) : ViewModel(){
    var state = MutableLiveData<RouteSaveState>()

    var coordinates = MutableLiveData<List<LatLng>>()

    private val resultChannel = Channel<RouteSaveResult<Unit>>()
    val authResults = resultChannel.receiveAsFlow()


    init {
        state.postValue(RouteSaveState())

    }

    fun onEvent(event: RouteSaveUiEvent) {
        when(event) {
            is RouteSaveUiEvent.RouteTitleChanged -> {
                state.value = state.value?.copy(routeTitle = event.value)
            }
            is RouteSaveUiEvent.RouteDescriptionChanged -> {
                state.value = state.value?.copy(routeDescription = event.value)
            }
            is RouteSaveUiEvent.Save -> {
                save()
            }
        }
    }

    private fun save() {
        viewModelScope.launch {
            state.value = state.value?.copy(isLoading = true)
            val result = repository.saveRoute(
                routeTitle = state.value!!.routeTitle,
                routeDescription = state.value!!.routeDescription,
                coordinates = coordinates.value!!
            )
            try{
                resultChannel.send(result)
            }
            catch (e: Exception){}
            state.value = state.value?.copy(isLoading = false)
        }
    }
}