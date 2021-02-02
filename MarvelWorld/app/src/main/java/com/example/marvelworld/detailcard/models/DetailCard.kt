package com.example.marvelworld.detailcard.models

import com.example.marvelworld.api.models.Image
import com.example.marvelworld.api.models.Url
import com.example.marvelworld.util.ResourceType

data class DetailCard(
    val resourceId: Int,
    val title: String,
    val thumbnail: Image?,
    val description: String?,
    val urls: List<Url>?,
    val type: ResourceType,
    val isFavorite: Boolean
)