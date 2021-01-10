package com.example.marvelworld.eventlist.repository

import com.example.marvelworld.api.utils.NetworkUtils

class EventRepository {
    private val eventService = NetworkUtils.getApiService(EventEndpoint::class.java)

    suspend fun getEvents(
        offset: Int,
        limit: Int,
        nameStartsWith: String?,
        characters: List<Int>,
        comics: List<Int>,
        events: List<Int>,
        series: List<Int>
    ) = eventService.getEvents(
        offset,
        limit,
        nameStartsWith,
        characters,
        comics,
        events,
        series
    )
}