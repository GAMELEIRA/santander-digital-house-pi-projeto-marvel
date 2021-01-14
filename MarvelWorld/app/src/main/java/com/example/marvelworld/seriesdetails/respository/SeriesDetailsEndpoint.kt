package com.example.marvelworld.seriesdetails.respository

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

interface SeriesDetailsEndpoint {
    @GET("/v1/public/series/{seriesId}")
    suspend fun getOneSeries(@Path("seriesId") seriesId: Int): DataWrapper<Series>

    @GET("/v1/public/series/{seriesId}/characters")
    suspend fun getSeriesCharacters(
        @Path("seriesId") seriesId: Int,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): DataWrapper<Character>

    @GET("/v1/public/series/{seriesId}/comics")
    suspend fun getSeriesComics(
        @Path("seriesId") seriesId: Int,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): DataWrapper<Comic>

    @GET("/v1/public/series/{seriesId}/creators")
    suspend fun getSeriesCreators(
        @Path("seriesId") seriesId: Int,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): DataWrapper<Creator>

    @GET("/v1/public/series/{seriesId}/events")
    suspend fun getSeriesEvents(
        @Path("seriesId") seriesId: Int,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): DataWrapper<Event>

    @GET("/v1/public/series/{seriesId}/stories")
    suspend fun getSeriesStories(
        @Path("seriesId") seriesId: Int,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): DataWrapper<Story>
}