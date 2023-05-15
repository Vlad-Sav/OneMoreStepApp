package kg.android.onemorestepapp.repository

import android.content.SharedPreferences
import com.google.android.gms.maps.model.LatLng
import kg.android.onemorestepapp.models.AuthResult
import kg.android.onemorestepapp.models.requests.RegisterRequest
import kg.android.onemorestepapp.models.requests.RouteSaveRequest
import kg.android.onemorestepapp.repository.base.IAuthRepository
import kg.android.onemorestepapp.service.apiservice.AuthApi
import kg.android.onemorestepapp.service.apiservice.RouteApi
import kg.android.onemorestepapp.ui.routerecording.RouteSaveResult
import retrofit2.HttpException

class RouteRecordRepository(private val api: RouteApi,
                            private val prefs: SharedPreferences
){
    suspend fun saveRoute(
        routeTitle: String,
        routeDescription: String,
        coordinates: List<LatLng>
    ): RouteSaveResult<Unit> {
        return try {
            val token = prefs.getString("jwt", null)
            api.saveRoute(
                "Bearer $token",
                routeSaveRequest = RouteSaveRequest(
                    routeTitle = routeTitle,
                    routeDescription = routeDescription,
                    coordinates = coordinates
                )
            )
        } catch (e: HttpException) {
            if (e.code() == 401) {
                RouteSaveResult.NotSaved()
            } else {
                RouteSaveResult.UnknownError(e.message ?: "Unknown error")
            }
        } catch (e: Exception) {
            RouteSaveResult.UnknownError(e.message ?: "Unknown error")
        }
    }
}