package com.example.marvelworld.eventdetails.respository

import com.example.marvelworld.api.utils.NetworkUtils

class EventDetailsRepository {
    private val eventService = NetworkUtils.getApiService(EventDetailsEndpoint::class.java)

    suspend fun getEvent(eventId: Int) = eventService.getEvent(eventId)
    suspend fun getEventCharacters(eventId: Int) = eventService.getEventCharacters(eventId)
    suspend fun getEventComics(eventId: Int) = eventService.getEventComics(eventId)
    suspend fun getEventCreators(eventId: Int) = eventService.getEventCreators(eventId)
    suspend fun getEventSeries(eventId: Int) = eventService.getEventSeries(eventId)
    suspend fun getEventStories(eventId: Int) = eventService.getEventStories(eventId)
}