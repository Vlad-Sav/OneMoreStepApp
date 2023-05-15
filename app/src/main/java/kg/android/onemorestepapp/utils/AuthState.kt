package kg.android.onemorestepapp.utils

data class AuthState(
    val isLoading: Boolean = false,
    val signUpEmail: String = "",
    val signUpUsername: String = "",
    val signUpPassword: String = "",
    val signUpConfirmPassword: String = "",
    val signInEmail: String = "",
    val signInPassword: String = ""
)