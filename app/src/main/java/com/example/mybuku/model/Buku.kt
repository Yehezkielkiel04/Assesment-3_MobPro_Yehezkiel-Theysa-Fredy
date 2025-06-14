package com.example.mybuku.model

data class Buku(
    val id: Int,
    val imagepath: String,
    val judul: String,
    val penulis: String,
    val upload_date: String,
    val imageUrl : String?= null
)
