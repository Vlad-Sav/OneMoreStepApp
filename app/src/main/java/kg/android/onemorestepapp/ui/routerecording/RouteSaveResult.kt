package kg.android.onemorestepapp.ui.routerecording

sealed class RouteSaveResult<T>(val data: T? = null) {
    class Saved<T>(data: T? = null): RouteSaveResult<T>(data)
    class NotSaved<T>(data: T? = null): RouteSaveResult<T>(data)
    class UnknownError<T>(val message: String = "unknown error"): RouteSaveResult<T>()
}