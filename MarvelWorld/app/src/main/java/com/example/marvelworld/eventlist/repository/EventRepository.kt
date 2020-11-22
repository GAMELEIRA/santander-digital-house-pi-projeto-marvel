package com.example.marvelworld.eventlist.repository

import com.example.marvelworld.api.utils.NetworkUtils

class EventRepository {
    private val eventService = NetworkUtils.getApiService(EventEndpoint::class.java)

    suspend fun getEvents() = eventService.getEvents()
}