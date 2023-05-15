package kg.android.onemorestepapp.repository

import android.content.SharedPreferences
import android.util.Log
import kg.android.onemorestepapp.models.responses.RandomStickerResponse
import kg.android.onemorestepapp.service.apiservice.RewardsApi
import retrofit2.HttpException

class RewardsRepository(private val api: RewardsApi,
                        private val prefs: SharedPreferences
){
    suspend fun getStickersNumber(): Int?{
        return try {
            val token = prefs.getString("jwt", null)
            Log.d("profileResponse", token ?: "empty token")
            val res = api.getStickersNumber("Bearer $token")
            return res.stickersNumber
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

    suspend fun getSticker(id: Int): String?{
        return try {
            val token = prefs.getString("jwt", null)
            Log.d("profileResponse", token ?: "empty token")
            val res = api.getSticker("Bearer $token", id)
            return res.sticker
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

    suspend fun getRandomSticker(): RandomStickerResponse?{
        return try {
            val token = prefs.getString("jwt", null)
            Log.d("profileResponse", token ?: "empty token")
            val res = api.getRandomSticker("Bearer $token")
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
}