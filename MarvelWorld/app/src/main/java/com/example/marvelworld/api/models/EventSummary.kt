package com.example.marvelworld.api.models

import com.example.marvelworld.reusablecomponents.HorizontalListItem

data class EventSummary(
    val resourceURI: String,
    override val name: String
) : HorizontalListItem