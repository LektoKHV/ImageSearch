package ru.vldkrt.imagesearch.data.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import ru.vldkrt.imagesearch.data.entities.ImageResultDataRemote

const val LOAD_SIZE = 100
const val API_KEY = "ea17e7f4915c83c603ed6a84f20eb40e29c9662c5a650f9580843332e15da1f0"

interface ImageClient {

    @GET("search.json")
    suspend fun getImages(
        @Query("engine") engine: String = "google",
        @Query("api_key") apiKey: String = API_KEY,
        @Query("tbm") tbm: String = "isch",
        @Query("device") device: String = "mobile",
//        @Query("num") pageSize: Int = LOAD_SIZE,
        @Query("q") query: String,
        @Query("ijn") page: Int,
    ): Response<ImageResultDataRemote>
}