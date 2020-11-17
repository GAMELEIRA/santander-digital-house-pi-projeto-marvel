package com.example.marvelworld.api.models

data class CharacterList(
    val available: Int,
    val returned: Int,
    val collectionURI: String,
    val items: List<CharacterSummary>
)