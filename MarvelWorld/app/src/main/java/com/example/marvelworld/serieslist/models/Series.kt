package com.example.marvelworld.serieslist.models

import com.example.marvelworld.api.models.Image
import com.example.marvelworld.api.models.Url

data class Series(
    val id: Int,
    val title: String,
    val description: String,
    val urls: List<Url>,
    val thumbnail: Image,
    var isFavorite: Boolean = false
)