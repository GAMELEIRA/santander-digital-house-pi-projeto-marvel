package com.example.marvelworld.creatorlist.models

import com.example.marvelworld.api.models.Image
import com.example.marvelworld.api.models.Url

data class Creator(
    val id: Int,
    val fullName: String,
    val urls: List<Url>,
    val thumbnail: Image,
    var isFavorite: Boolean = false
)