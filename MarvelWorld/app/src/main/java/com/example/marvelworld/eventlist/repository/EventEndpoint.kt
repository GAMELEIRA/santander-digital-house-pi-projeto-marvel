package com.example.marvelworld.eventlist.repository

import com.example.marvelworld.api.models.DataWrapper
import com.example.marvelworld.eventlist.models.Event
import retrofit2.http.GET
import retrofit2.http.Path

interface EventEndpoint {
    @GET("/v1/public/events")
    suspend fun getEvents(): DataWrapper<Event>
}