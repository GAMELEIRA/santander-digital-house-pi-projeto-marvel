package com.example.marvelworld.creatorlist.respository

import com.example.marvelworld.api.models.DataWrapper
import com.example.marvelworld.creatorlist.models.Creator
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CreatorEndpoint {
    @GET("/v1/public/creators")
    suspend fun getCreators(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int,
        @Query("nameStartsWith") nameStartsWith: String?,
        @Query("comics") comics: List<Int>,
        @Query("events") events: List<Int>,
        @Query("series") series: List<Int>
    ): DataWrapper<Creator>

    @GET("/v1/public/creators/{creatorId}")
    suspend fun getCreator(@Path("creatorId") creatorId: Int): DataWrapper<Creator>
}