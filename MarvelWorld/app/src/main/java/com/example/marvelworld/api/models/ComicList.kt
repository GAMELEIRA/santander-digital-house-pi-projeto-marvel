package com.example.marvelworld.api.models

data class ComicList(
    val available: Int,
    val returned: Int,
    val collectionURI: String,
    val items: List<ComicSummary>
)