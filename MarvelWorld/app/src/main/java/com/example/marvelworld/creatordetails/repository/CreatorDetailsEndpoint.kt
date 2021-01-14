package com.example.marvelworld.creatordetails.repository

import com.example.marvelworld.api.models.DataWrapper
import com.example.marvelworld.comiclist.models.Comic
import com.example.marvelworld.creatorlist.models.Creator
import com.example.marvelworld.eventlist.models.Event
import com.example.marvelworld.serieslist.models.Series
import com.example.marvelworld.storylist.models.Story
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CreatorDetailsEndpoint {
    @GET("/v1/public/creators/{creatorId}")
    suspend fun getCreator(@Path("creatorId") creatorId: Int): DataWrapper<Creator>

    @GET("/v1/public/creators/{creatorId}/comics")
    suspend fun getCreatorComics(
        @Path("creatorId") creatorId: Int,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): DataWrapper<Comic>

    @GET("/v1/public/creators/{creatorId}/events")
    suspend fun getCreatorEvents(
        @Path("creatorId") creatorId: Int,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): DataWrapper<Event>

    @GET("/v1/public/creators/{creatorId}/series")
    suspend fun getCreatorSeries(
        @Path("creatorId") creatorId: Int,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): DataWrapper<Series>

    @GET("/v1/public/creators/{creatorId}/stories")
    suspend fun getCreatorStories(
        @Path("creatorId") creatorId: Int,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): DataWrapper<Story>
}