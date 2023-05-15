package kg.android.onemorestepapp.service.apiservice

import kg.android.onemorestepapp.models.responses.IntResponse
import kg.android.onemorestepapp.models.responses.TopUsersResponse
import kg.android.onemorestepapp.models.responses.UserProfileResponse
import kg.android.onemorestepapp.models.responses.UsersPinnedStickerResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header

interface ProfileApi {
    @GET("User/UserProfile")
    suspend fun userProfile(
        @Header("Authorization") token: String
    ): UserProfileResponse

    @GET("Stickers/UsersPinnedSticker")
    suspend fun usersPinnedSticker(
        @Header("Authorization") token: String
    ): UsersPinnedStickerResponse

    @GET("User/TopUsers")
    suspend fun getTopUsers(
        @Header("Authorization") token: String
    ): List<TopUsersResponse>?

    @GET("Steps/Level")
    suspend fun getLevel(
        @Header("Authorization") token: String
    ): IntResponse?

    @GET("Steps/Progress")
    suspend fun getProgress(
        @Header("Authorization") token: String
    ): IntResponse?
}