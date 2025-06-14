package com.example.mybuku.network

import com.example.mybuku.model.BukuResponse
import com.example.mybuku.model.GeneralAPIResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query


private const val BASE_URL = "https://kogenkode.my.id/kiel/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface BukuApiService {

    @GET("api.php")
    suspend fun getBuku(
        @Header("Authorization") userId: String
    ): BukuResponse

    @DELETE("api.php")
    suspend fun deleteBuku(
        @Header("Authorization") userId: String,
        @Query("id") id: Int
    ): GeneralAPIResponse

    @Multipart
    @POST("api.php")
    suspend fun postBuku(
        @Header("Authorization") userId: String,
        @Part("judul") judul: RequestBody,
        @Part("penulis") penulis: RequestBody,
        @Part gambar: MultipartBody.Part?
    ): GeneralAPIResponse
}

object BukuApi {
    val service: BukuApiService by lazy {
        retrofit.create(BukuApiService::class.java)
    }

    fun getBukuUrl(imagePath: String): String {
        return BASE_URL + imagePath
    }
}

enum class ApiStatus { LOADING, SUCCESS, FAILED }
