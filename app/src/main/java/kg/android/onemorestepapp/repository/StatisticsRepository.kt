package kg.android.onemorestepapp.repository

import android.content.SharedPreferences
import kg.android.onemorestepapp.models.responses.IntResponse
import kg.android.onemorestepapp.models.responses.RoutesResponse
import kg.android.onemorestepapp.models.responses.TopUsersResponse
import kg.android.onemorestepapp.service.apiservice.ProfileApi
import kg.android.onemorestepapp.service.apiservice.RouteApi
import retrofit2.HttpException

class StatisticsRepository(private val api: ProfileApi,
                             private val prefs: SharedPreferences
){
    suspend fun getTopUsers(): List<TopUsersResponse>? {
        return try {
            val token = prefs.getString("jwt", null)
            val res = api.getTopUsers(
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

    suspend fun getLevel(): IntResponse? {
        return try {
            val token = prefs.getString("jwt", null)
            val res = api.getLevel(
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

    suspend fun getProgress(): IntResponse? {
        return try {
            val token = prefs.getString("jwt", null)
            val res = api.getProgress(
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