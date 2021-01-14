package com.example.marvelworld.eventdetails.respository

import com.example.marvelworld.api.utils.NetworkUtils

class EventDetailsRepository {
    private val eventService = NetworkUtils.getApiService(EventDetailsEndpoint::class.java)

    suspend fun getEvent(eventId: Int) = eventService.getEvent(eventId)

    suspend fun getEventCharacters(offset: Int, limit: Int, eventId: Int) =
        eventService.getEventCharacters(eventId,offset, limit)

    suspend fun getEventComics(offset: Int, limit: Int, eventId: Int) =
        eventService.getEventComics(eventId,offset, limit)

    suspend fun getEventCreators(offset: Int, limit: Int, eventId: Int) =
        eventService.getEventCreators(eventId,offset, limit)

    suspend fun getEventSeries(offset: Int, limit: Int, eventId: Int) =
        eventService.getEventSeries(eventId,offset, limit)

    suspend fun getEventStories(offset: Int, limit: Int, eventId: Int) =
        eventService.getEventStories(eventId,offset, limit)
}