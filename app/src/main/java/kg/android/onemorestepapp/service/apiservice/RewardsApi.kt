package kg.android.onemorestepapp.service.apiservice

import kg.android.onemorestepapp.models.responses.*
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface RewardsApi {
    @GET("Stickers/StickersNumber")
    suspend fun getStickersNumber(
        @Header("Authorization") token: String
    ): RewardsNumberResponse

    @GET("Stickers/Sticker/{Id}")
    suspend fun getSticker(
        @Header("Authorization") token: String,
        @Path("Id") id: Int
    ): StickerResponse

    @GET("Stickers/RandomSticker")
    suspend fun getRandomSticker(
        @Header("Authorization") token: String
    ): RandomStickerResponse
}