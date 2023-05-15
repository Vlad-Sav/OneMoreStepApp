package kg.android.onemorestepapp.service.apiservice

import kg.android.onemorestepapp.models.requests.LoginRequest
import kg.android.onemorestepapp.models.requests.RegisterRequest
import kg.android.onemorestepapp.models.responses.JwtTokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApi {
    @POST("Authorization/Register")
    suspend fun register(
        @Body registerRequest: RegisterRequest,
    )

    @POST("Authorization/Login")
    suspend fun login(
        @Body loginRequest: LoginRequest,
    ): JwtTokenResponse

    @GET("Authorization/refresh")
    suspend fun refreshToken(
        @Header("Authorization") token: String,
    )
}