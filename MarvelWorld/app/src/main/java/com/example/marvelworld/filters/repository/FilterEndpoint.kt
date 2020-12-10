package com.example.marvelworld.filters.repository

import com.example.marvelworld.api.models.DataWrapper
import com.example.marvelworld.filters.models.FilterServiceResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface FilterEndpoint {
    @GET("/v1/public/characters")
    suspend fun getCharacters(@Query("nameStartsWith") nameStartsWith: String): DataWrapper<FilterServiceResponse>

    @GET("/v1/public/comics")
    suspend fun getComics(@Query("titleStartsWith") titleStartsWith: String): DataWrapper<FilterServiceResponse>

    @GET("/v1/public/events")
    suspend fun getEvents(@Query("nameStartsWith") nameStartsWith: String): DataWrapper<FilterServiceResponse>

    @GET("/v1/public/series")
    suspend fun getSeries(@Query("titleStartsWith") titleStartsWith: String): DataWrapper<FilterServiceResponse>

    @GET("/v1/public/creators")
    suspend fun getCreators(@Query("nameStartsWith") nameStartsWith: String): DataWrapper<FilterServiceResponse>
}