package com.example.mybuku.model

data class GeneralAPIResponse(
    var status: String,
    var message: String? = "",
    val id: Int? = null,
    val imagePath: String? = null,
    val judul: String? = null,
    val penulis: String? = null,
    val uploadDate: String? = null,
    val imageUrl: String? = null
)
