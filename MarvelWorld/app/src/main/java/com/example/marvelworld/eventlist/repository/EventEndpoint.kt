package com.example.marvelworld.eventlist.repository

import com.example.marvelworld.api.models.DataWrapper
import com.example.marvelworld.eventlist.models.Event
import retrofit2.http.GET
import retrofit2.http.Path

interface EventEndpoint {
    @GET("/v1/public/events")
    suspend fun getEvents(): DataWrapper<Event>

    @GET("/v1/public/events/{eventId}")
    suspend fun getEvent(@Path("eventId") eventId: Int): DataWrapper<Event>

    @GET("/v1/public/events/{eventId}/characters")
    suspend fun getEventCharacters(@Path("eventId") eventId: Int): DataWrapper<Event>

    @GET("/v1/public/events/{eventId}/comics")
    suspend fun getEventComics(@Path("eventId") eventId: Int): DataWrapper<Event>

    @GET("/v1/public/events/{eventId}/creators")
    suspend fun getEventCreators(@Path("eventId") eventId: Int): DataWrapper<Event>

    @GET("/v1/public/events/{eventId}/series")
    suspend fun getEventSeries(@Path("eventId") eventId: Int): DataWrapper<Event>

    @GET("/v1/public/events/{eventId}/stories")
    suspend fun getEventStories(@Path("eventId") eventId: Int): DataWrapper<Event>
}