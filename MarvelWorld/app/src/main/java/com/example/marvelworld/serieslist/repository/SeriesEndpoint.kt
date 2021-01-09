package com.example.marvelworld.serieslist.repository

import com.example.marvelworld.api.models.DataWrapper
import com.example.marvelworld.serieslist.models.Series
import retrofit2.http.GET
import retrofit2.http.Query

interface SeriesEndpoint {
    @GET("/v1/public/series")
    suspend fun getSeries(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int,
        @Query("titleStartsWith") titleStartsWith: String?,
        @Query("characters") characters: List<Int>,
        @Query("comics") comics: List<Int>,
        @Query("events") events: List<Int>,
        @Query("creators") creators: List<Int>
    ): DataWrapper<Series>
}