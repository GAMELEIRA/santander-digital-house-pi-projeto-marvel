package com.example.marvelworld.comiclist.models

import com.example.marvelworld.api.models.Image
import com.example.marvelworld.api.models.Url

data class Comic(
    val id: Int,
    val title: String,
    val description: String?,
    val dates: List<ComicDate>,
    val urls: List<Url>,
    val thumbnail: Image,
    var isFavorite: Boolean = false
)