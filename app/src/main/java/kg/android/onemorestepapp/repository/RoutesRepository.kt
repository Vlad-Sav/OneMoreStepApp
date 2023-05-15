package kg.android.onemorestepapp.repository

import android.content.SharedPreferences
import com.google.android.gms.maps.model.LatLng
import kg.android.onemorestepapp.models.requests.RouteSaveRequest
import kg.android.onemorestepapp.models.responses.RoutesResponse
import kg.android.onemorestepapp.service.apiservice.RouteApi
import kg.android.onemorestepapp.ui.routerecording.RouteSaveResult
import retrofit2.HttpException

class RoutesRepository (private val api: RouteApi,
                        private val prefs: SharedPreferences
){
    suspend fun getRoutes(): List<RoutesResponse>? {
        return try {
            val token = prefs.getString("jwt", null)
            val res = api.getRoutes(
                "Bearer $token"
            )
            res
        } catch (e: HttpException) {
            if (e.code() == 401) {
                return null
            } else {
                //RouteSaveResult.UnknownError(e.message ?: "Unknown error")
                return null
            }
        } catch (e: Exception) {
            return null
        }
    }
}