package kg.android.onemorestepapp.models.responses

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RoutesResponse(val id: Int, val userId: Int, val userName: String, val title: String, val description: String, val locationList: List<LatLng>): Parcelable