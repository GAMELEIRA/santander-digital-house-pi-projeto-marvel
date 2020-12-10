package com.example.marvelworld.eventlist.repository

import com.example.marvelworld.api.models.DataWrapper
import com.example.marvelworld.eventlist.models.Event
import retrofit2.http.GET
import retrofit2.http.Query

interface EventEndpoint {
    @GET("/v1/public/events")
    suspend fun getEvents(
        @Query("nameStartsWith") nameStartsWith: String?,
        @Query("characters") characters: List<Int>,
        @Query("comics") comics: List<Int>,
        @Query("events") events: List<Int>,
        @Query("series") series: List<Int>
    ): DataWrapper<Event>
}