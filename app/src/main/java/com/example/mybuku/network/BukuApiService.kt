package com.example.mybuku.network

import com.example.mybuku.model.BukuResponse
import com.example.mybuku.model.OpStatus
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

private const val BASE_URL = "https://mybuku.infinityfreeapp.com/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface BukuApiService {
    @GET("buku.php")
    suspend fun getBuku(
        @Query("userId") userId: String
    ): BukuResponse

    @DELETE("buku.php")
    suspend fun deleteBuku(
        @Query("userId") userId: String,
        @Query("id") id: String
    ): OpStatus

    @Multipart
    @POST("buku.php")
    suspend fun postBuku(
        @Query("userId") userId: String,
        @Part("judul") judul: RequestBody,
        @Part("penulis") penulis: RequestBody,
        @Part image: MultipartBody.Part
    ): OpStatus
}

object BukuApi {
    val service: BukuApiService by lazy {
        retrofit.create(BukuApiService::class.java)
    }

    fun getBukuUrl(imageId: String): String {
        return "${BASE_URL}upload/$imageId"
    }
}

enum class ApiStatus { LOADING, SUCCESS, FAILED }
