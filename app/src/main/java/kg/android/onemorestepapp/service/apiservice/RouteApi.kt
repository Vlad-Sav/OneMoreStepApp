package kg.android.onemorestepapp.service.apiservice

import kg.android.onemorestepapp.models.requests.RegisterRequest
import kg.android.onemorestepapp.models.requests.RouteSaveRequest
import kg.android.onemorestepapp.models.responses.RoutesResponse
import kg.android.onemorestepapp.ui.routerecording.RouteSaveResult
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface RouteApi {
    @POST("Route/")
    suspend fun saveRoute(
        @Header("Authorization") token: String,
        @Body routeSaveRequest: RouteSaveRequest
    ): RouteSaveResult<Unit>

    @GET("Route/")
    suspend fun getRoutes(
        @Header("Authorization") token: String
    ): List<RoutesResponse>?
}