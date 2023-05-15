package kg.android.onemorestepapp.repository

import android.content.SharedPreferences
import kg.android.onemorestepapp.models.AuthResult
import kg.android.onemorestepapp.models.requests.LoginRequest
import kg.android.onemorestepapp.models.requests.RegisterRequest
import kg.android.onemorestepapp.repository.base.IAuthRepository
import kg.android.onemorestepapp.service.apiservice.AuthApi
import retrofit2.HttpException

class AuthRepository(private val api: AuthApi,
                     private val prefs: SharedPreferences
): IAuthRepository {
    override suspend fun register(username: String,
                                  email: String,
                                  password: String,
                                  confirmPassword: String
    ): AuthResult<Unit> {
        return try {
            api.register(
                registerRequest = RegisterRequest(
                    email = email,
                    username = username,
                    password = password,
                    confirmPassword = confirmPassword
                )
            )
            login(email, password)
        } catch(e: HttpException) {
            if(e.code() == 401) {
                AuthResult.Unauthorized()
            } else {
                AuthResult.UnknownError(e.message ?: "Unknown error")
            }
        } catch (e: Exception) {
            AuthResult.UnknownError(e.message ?: "Unknown error")
        }
    }

    override suspend fun login(email: String, password: String): AuthResult<Unit> {
        return try {
            val response = api.login(
                loginRequest = LoginRequest(
                    email = email,
                    password = password
                )
            )
            prefs.edit()
                .putString("jwt", response.token)
                .apply()
            AuthResult.Authorized()
        } catch(e: HttpException) {
            if(e.code() == 401) {
                AuthResult.Unauthorized()
            } else {
                AuthResult.UnknownError(e.message ?: "Unknown error")
            }
        } catch (e: Exception) {
            AuthResult.UnknownError(e.message ?: "Unknown error")
        }
    }

    override suspend fun authenticate(): AuthResult<Unit> {
        return try {
            val token = prefs.getString("jwt", null) ?: return AuthResult.Unauthorized()
            //api.authenticate("Bearer $token")
            AuthResult.Authorized()
        } catch(e: HttpException) {
            if(e.code() == 401) {
                AuthResult.Unauthorized()
            } else {
                AuthResult.UnknownError()
            }
        } catch (e: Exception) {
            AuthResult.UnknownError()
        }
    }
}