package kg.android.onemorestepapp.models

sealed class AuthResult<T>(val data: T? = null) {
    class Authorized<T>(data: T? = null): AuthResult<T>(data)
    class Unauthorized<T>: AuthResult<T>()
    class UnknownError<T>(val message: String = "unknown error"): AuthResult<T>()
}