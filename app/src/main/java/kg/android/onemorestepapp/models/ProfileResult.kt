package kg.android.onemorestepapp.models

sealed class ProfileResult<T>(val data: T? = null) {
    class ProfileFetched<T>(data: T? = null): ProfileResult<T>(data)
    class ProfileNotFetched<T>: ProfileResult<T>()
    class UnknownError<T>(val message: String = "unknown error"): ProfileResult<T>()
}