package com.example.marvelworld.api.models

class Image(
    private val path: String,
    private val extension: String
) {
    fun getImagePath(imageResolution: String? = "detail"): String {
        return "$path/$imageResolution.$extension".replace("http://", "https://")
    }
}
