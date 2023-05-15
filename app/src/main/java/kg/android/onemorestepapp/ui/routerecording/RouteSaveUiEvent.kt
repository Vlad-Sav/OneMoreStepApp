package kg.android.onemorestepapp.ui.routerecording

sealed class RouteSaveUiEvent {
    data class RouteTitleChanged(val value: String): RouteSaveUiEvent()
    data class RouteDescriptionChanged(val value: String): RouteSaveUiEvent()
    object Save: RouteSaveUiEvent()
}