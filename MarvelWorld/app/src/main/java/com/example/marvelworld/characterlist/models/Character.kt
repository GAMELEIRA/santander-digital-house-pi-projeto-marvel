package com.example.marvelworld.characterlist.models

import com.example.marvelworld.api.models.Image
import com.example.marvelworld.api.models.Url

data class Character(
    val id: Int,
    val name: String,
    val description: String,
    val urls: List<Url>,
    val thumbnail: Image,
    var isFavorite: Boolean = false
)