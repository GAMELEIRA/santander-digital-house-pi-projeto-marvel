package com.example.marvelworld.eventlist.repository

import com.example.marvelworld.api.utils.NetworkUtils

class EventRepository {
    private val eventService = NetworkUtils.getApiService(EventEndpoint::class.java)

    suspend fun getEvents() = eventService.getEvents()
    suspend fun getEvent(eventId: Int) = eventService.getEvent(eventId)
    suspend fun getEventCharacters(eventId: Int) = eventService.getEventCharacters(eventId)
    suspend fun getEventComics(eventId: Int) = eventService.getEventComics(eventId)
    suspend fun getEventCreators(eventId: Int) = eventService.getEventCreators(eventId)
    suspend fun getEventSeries(eventId: Int) = eventService.getEventSeries(eventId)
    suspend fun getEventStories(eventId: Int) = eventService.getEventStories(eventId)
}