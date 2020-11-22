package com.example.marvelworld.serieslist.repository

import com.example.marvelworld.api.models.DataWrapper
import com.example.marvelworld.serieslist.models.Series
import retrofit2.http.GET
import retrofit2.http.Path

interface SeriesEndpoint {
    @GET("/v1/public/series")
    suspend fun getSeries(): DataWrapper<Series>
}