package kg.android.onemorestepapp.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kg.android.onemorestepapp.models.AuthResult
import kg.android.onemorestepapp.repository.AuthRepository
import kg.android.onemorestepapp.ui.auth.AuthUiEvent
import kg.android.onemorestepapp.utils.AuthState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repository: AuthRepository
): ViewModel() {
    var state = MutableLiveData<AuthState>()

    private val resultChannel = Channel<AuthResult<Unit>>()
    val authResults = resultChannel.receiveAsFlow()

    init {
        state.postValue(AuthState())
        authenticate()

    }

    fun onEvent(event: AuthUiEvent) {
        when(event) {
            is AuthUiEvent.SignInEmailChanged -> {
                state.value = state.value?.copy(signInEmail = event.value)
            }
            is AuthUiEvent.SignInPasswordChanged -> {
                state.value = state.value?.copy(signInPassword = event.value)
            }
            is AuthUiEvent.SignIn -> {
                signIn()
            }
            is AuthUiEvent.SignUpEmailChanged -> {
                state.value = state.value?.copy(signUpEmail = event.value)
            }
            is AuthUiEvent.SignUpUsernameChanged -> {
                state.value = state.value?.copy(signUpUsername = event.value)
            }
            is AuthUiEvent.SignUpPasswordChanged -> {
                state.value = state.value?.copy(signUpPassword = event.value)
            }
            is AuthUiEvent.SignUpConfirmPasswordChanged -> {
                state.value = state.value?.copy(signUpConfirmPassword = event.value)
            }
            is AuthUiEvent.SignUp -> {
                signUp()
            }
            is AuthUiEvent.Authenticate -> {
                authenticate()
            }
        }
    }
 /*   fun observeAuthResults(owner: LifecycleOwner, observer: suspend (AuthResult<Unit>) -> Unit) {
        authResults.onEach { observer(it) }.launchIn(owner.lifecycleScope)
    }*/
    private fun signUp() {
        viewModelScope.launch {
            state.value = state.value?.copy(isLoading = true)
            val result = repository.register(
                username = state.value!!.signUpUsername,
                email = state.value!!.signUpEmail,
                password = state.value!!.signUpPassword,
                confirmPassword = state.value!!.signUpConfirmPassword
            )
            resultChannel.send(result)
            state.value = state.value?.copy(isLoading = false)
        }
    }

    private fun signIn() {
        viewModelScope.launch {
            state.value = state.value?.copy(isLoading = true)
            val result = repository.login(
                email = state.value!!.signInEmail,
                password = state.value!!.signInPassword
            )
            try{
                resultChannel.send(result)
            }
            catch (e: Exception){

            }
            state.value = state.value?.copy(isLoading = false)
        }
    }

    private fun authenticate() {
        viewModelScope.launch {
            state.value = state.value?.copy(isLoading = true)
            val result = repository.authenticate()
            resultChannel.send(result)

            state.value = state.value?.copy(isLoading = false)
        }
    }
}