package kg.android.onemorestepapp.repository

import android.content.SharedPreferences
import android.util.Log
import kg.android.onemorestepapp.models.responses.UserProfileResponse
import kg.android.onemorestepapp.models.responses.UsersPinnedStickerResponse
import kg.android.onemorestepapp.service.apiservice.ProfileApi
import retrofit2.HttpException

class ProfileRepository (private val api: ProfileApi,
                         private val prefs: SharedPreferences
){
    suspend fun getUserProfile(): UserProfileResponse?{
        return try {
            val token = prefs.getString("jwt", null)
            Log.d("profileResponse", token ?: "empty token")
            val res = api.userProfile("Bearer $token")
            return res
        } catch (e: HttpException) {
            if (e.code() == 401) {
                Log.d("profileResponse", e.message())
                return null
            } else {
                Log.d("profileResponse", e.message())
                return null
            }
        } catch (e: Exception) {
            Log.d("profileResponse", e.message ?: "Unknown Error")
            return null
        }
    }

    suspend fun getUsersPinnedSticker(): UsersPinnedStickerResponse?{
        return try {
            val token = prefs.getString("jwt", null)
            Log.d("profileResponse", token ?: "empty token")
            val res = api.usersPinnedSticker("Bearer $token")
            return res
        } catch (e: HttpException) {
            if (e.code() == 401) {
                Log.d("profileResponse", e.message())
                return null
            } else {
                Log.d("profileResponse", e.message())
                return null
            }
        } catch (e: Exception) {
            Log.d("profileResponse", e.message ?: "Unknown Error")
            return null
        }
    }

    suspend fun logout(){
        prefs.edit().putString("jwt", null).apply()
    }

}