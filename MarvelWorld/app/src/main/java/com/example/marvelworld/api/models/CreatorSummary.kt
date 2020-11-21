package com.example.marvelworld.api.models

import com.example.marvelworld.reusablecomponents.HorizontalListItem

data class CreatorSummary(
    val resourceURI: String,
    override val name: String,
    val role: String
) : HorizontalListItem