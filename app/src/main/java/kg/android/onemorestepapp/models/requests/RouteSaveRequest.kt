package kg.android.onemorestepapp.models.requests

import com.google.android.gms.maps.model.LatLng

data class RouteSaveRequest(
    val routeTitle: String,
    val routeDescription: String,
    val coordinates: List<LatLng>
)