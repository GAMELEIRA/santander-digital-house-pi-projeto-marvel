package com.example.marvelworld.serieslist.repository

import com.example.marvelworld.api.models.DataWrapper
import com.example.marvelworld.serieslist.models.Series
import retrofit2.http.GET
import retrofit2.http.Path

interface SeriesEndpoint {
    @GET("/v1/public/series")
    suspend fun getSeries(): DataWrapper<Series>

    @GET("/v1/public/series/{seriesId}")
    suspend fun getOneSeries(@Path("seriesId") seriesId: Int): DataWrapper<Series>

    @GET("/v1/public/series/{seriesId}/characters")
    suspend fun getSeriesCharacters(@Path("seriesId") seriesId: Int): DataWrapper<Series>

    @GET("/v1/public/series/{seriesId}/comics")
    suspend fun getSeriesComics(@Path("seriesId") seriesId: Int): DataWrapper<Series>

    @GET("/v1/public/series/{seriesId}/creators")
    suspend fun getSeriesCreators(@Path("seriesId") seriesId: Int): DataWrapper<Series>

    @GET("/v1/public/series/{seriesId}/events")
    suspend fun getSeriesEvents(@Path("seriesId") seriesId: Int): DataWrapper<Series>

    @GET("/v1/public/series/{seriesId}/stories")
    suspend fun getSeriesStories(@Path("seriesId") seriesId: Int): DataWrapper<Series>
}