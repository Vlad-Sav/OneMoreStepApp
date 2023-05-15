package kg.android.onemorestepapp.models

sealed class RoutesResult<T>(val data: T? = null) {
    class RoutesFetched<T>(data: T? = null): RoutesResult<T>(data)
    class ProfileNotFetched<T>: RoutesResult<T>()
    class UnknownError<T>(val message: String = "unknown error"): RoutesResult<T>()
}