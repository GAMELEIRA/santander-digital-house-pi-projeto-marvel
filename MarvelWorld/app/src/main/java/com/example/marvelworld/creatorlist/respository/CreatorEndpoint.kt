package com.example.marvelworld.creatorlist.respository

import com.example.marvelworld.api.models.DataWrapper
import com.example.marvelworld.creatorlist.models.Creator
import retrofit2.http.GET
import retrofit2.http.Path

interface CreatorEndpoint {
    @GET("/v1/public/creators")
    suspend fun getCreators(): DataWrapper<Creator>

    @GET("/v1/public/creators/{creatorId}")
    suspend fun getCreator(@Path("creatorId") creatorId: Int): DataWrapper<Creator>

    @GET("/v1/public/creators/{creatorId}/comics")
    suspend fun getCreatorComics(@Path("creatorId") creatorId: Int): DataWrapper<Creator>

    @GET("/v1/public/creators/{creatorId}/events")
    suspend fun getCreatorEvents(@Path("creatorId") creatorId: Int): DataWrapper<Creator>

    @GET("/v1/public/creators/{creatorId}/series")
    suspend fun getCreatorSeries(@Path("creatorId") creatorId: Int): DataWrapper<Creator>

    @GET("/v1/public/creators/{creatorId}/stories")
    suspend fun getCreatorStories(@Path("creatorId") creatorId: Int): DataWrapper<Creator>
}