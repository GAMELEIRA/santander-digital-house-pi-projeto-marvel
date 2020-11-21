package com.example.marvelworld.api.models

data class SeriesList(
    val available: Int,
    val returned: Int,
    val collectionURI: String,
    val items: List<SeriesSummary>
)