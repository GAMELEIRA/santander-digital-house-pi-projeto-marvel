package com.example.marvelworld.comiclist.repository

import com.example.marvelworld.api.models.DataWrapper
import com.example.marvelworld.comiclist.models.Comic
import retrofit2.http.GET
import retrofit2.http.Query

interface ComicEndpoint {
    @GET("/v1/public/comics")
    suspend fun getComics(
        @Query("titleStartsWith") titleStartsWith: String?,
        @Query("characters") characters: List<Int>,
        @Query("events") events: List<Int>,
        @Query("series") series: List<Int>,
        @Query("creators") creators: List<Int>
    ): DataWrapper<Comic>
}