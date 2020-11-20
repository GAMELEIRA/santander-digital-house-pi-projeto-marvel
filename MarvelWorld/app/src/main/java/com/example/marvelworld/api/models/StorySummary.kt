package com.example.marvelworld.api.models

import com.example.marvelworld.reusablecomponents.HorizontalListItem

data class StorySummary(
    val resourceURI: String,
    override val name: String,
    val type: String
) :HorizontalListItem
