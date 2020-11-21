package com.example.marvelworld.api.models

data class DataWrapper<T>(
    val code: Int,
    val status: String,
    val data: DataContainer<T>,
    val etag: String,
    val copyright: String,
    val attributionHTML: String
)