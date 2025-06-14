package com.example.mybuku.ui.screen

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mybuku.model.Buku
import com.example.mybuku.network.ApiStatus
import com.example.mybuku.network.BukuApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream

class MainViewModel : ViewModel() {

    var data = mutableStateOf(emptyList<Buku>())
        private set

    var status = MutableStateFlow(ApiStatus.LOADING)
        private set

    var errorMessage = mutableStateOf<String?>(null)
        private set

    fun retrieveData(userEmail: String) {
        viewModelScope.launch(Dispatchers.IO) {
            status.value = ApiStatus.LOADING
            try {
                val response = BukuApi.service.getBuku(userEmail)
                data.value = response.data
                status.value = ApiStatus.SUCCESS
            } catch (e: Exception) {
                Log.d("MainViewModel", "Failure: ${e.message}")
                status.value = ApiStatus.FAILED
                errorMessage.value = e.message
            }
        }
    }

    fun saveData(userEmail: String, judul: String, penulis: String, bitmap: Bitmap) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = BukuApi.service.postBuku(
                    userEmail,
                    judul.toRequestBody("text/plain".toMediaTypeOrNull()),
                    penulis.toRequestBody("text/plain".toMediaTypeOrNull()),
                    bitmap.toMultipartBody()
                )

                if (result.status == "success")
                    retrieveData(userEmail)
                else
                    throw Exception(result.message)
            } catch (e: Exception) {
                Log.d("MainViewModel", "Failure: ${e.message}")
                errorMessage.value = "Error: ${e.message}"
            }
        }
    }


    fun deleteBuku(userEmail: String, id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = BukuApi.service.deleteBuku(userEmail, id)
                if (result.status == "success") {
                    retrieveData(userEmail)
                } else {
                    throw Exception(result.message)
                }
            } catch (e: Exception) {
                Log.e("MainViewModel", "Delete failed: ${e.message}")
                errorMessage.value = "Gagal hapus: ${e.message}"
            }
        }
    }

    fun clearMessage() {
        errorMessage.value = null
    }

    private fun Bitmap.toMultipartBody(): MultipartBody.Part {
        val stream = ByteArrayOutputStream()
        compress(Bitmap.CompressFormat.JPEG, 80, stream)
        val byteArray = stream.toByteArray()
        val requestBody = byteArray.toRequestBody("image/jpg".toMediaTypeOrNull(), 0, byteArray.size)
        return MultipartBody.Part.createFormData("gambar", "buku.jpg", requestBody)
    }
}
