package com.example.marvelworld.api.models

data class EventList(
    val available: Int,
    val returned: Int,
    val collectionURI: String,
    val items: List<EventSummary>
)