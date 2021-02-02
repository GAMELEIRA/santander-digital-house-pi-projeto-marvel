package com.example.marvelworld.storylist.models

import com.example.marvelworld.api.models.Image

data class Story(
    val id: Int,
    val title: String,
    val description: String,
    val thumbnail: Image?,
    var isFavorite: Boolean = false
)