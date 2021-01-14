package com.example.marvelworld.eventdetails.respository

import com.example.marvelworld.api.models.DataWrapper
import com.example.marvelworld.characterlist.models.Character
import com.example.marvelworld.comiclist.models.Comic
import com.example.marvelworld.creatorlist.models.Creator
import com.example.marvelworld.eventlist.models.Event
import com.example.marvelworld.serieslist.models.Series
import com.example.marvelworld.storylist.models.Story
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface EventDetailsEndpoint {
    @GET("/v1/public/events/{eventId}")
    suspend fun getEvent(@Path("eventId") eventId: Int): DataWrapper<Event>

    @GET("/v1/public/events/{eventId}/characters")
    suspend fun getEventCharacters(
        @Path("eventId") eventId: Int,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): DataWrapper<Character>

    @GET("/v1/public/events/{eventId}/comics")
    suspend fun getEventComics(
        @Path("eventId") eventId: Int,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): DataWrapper<Comic>

    @GET("/v1/public/events/{eventId}/creators")
    suspend fun getEventCreators(
        @Path("eventId") eventId: Int,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): DataWrapper<Creator>

    @GET("/v1/public/events/{eventId}/series")
    suspend fun getEventSeries(
        @Path("eventId") eventId: Int,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): DataWrapper<Series>

    @GET("/v1/public/events/{eventId}/stories")
    suspend fun getEventStories(
        @Path("eventId") eventId: Int,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): DataWrapper<Story>
}