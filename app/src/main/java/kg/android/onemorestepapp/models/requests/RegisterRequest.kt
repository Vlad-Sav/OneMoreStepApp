package kg.android.onemorestepapp.models.requests

data class RegisterRequest(val email: String,
                           val username: String,
                           val password: String,
                           val confirmPassword: String)
