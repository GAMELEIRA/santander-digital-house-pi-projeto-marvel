package com.example.marvelworld.reusablecomponents.expandablecard

import com.example.marvelworld.api.models.Url

data class Card(
    val title: String,
    val imageCard: String?,
    val imageDialog: String?,
    val description: String?,
    val urls: List<Url>?
)