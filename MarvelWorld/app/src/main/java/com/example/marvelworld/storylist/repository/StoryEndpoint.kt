package com.example.marvelworld.storylist.repository

import com.example.marvelworld.api.models.DataWrapper
import com.example.marvelworld.storylist.models.Story
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface StoryEndpoint {
    @GET("/v1/public/stories")
    suspend fun getStories(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int,
        @Query("characters") characters: List<Int>,
        @Query("comics") comics: List<Int>,
        @Query("events") events: List<Int>,
        @Query("series") series: List<Int>,
        @Query("creators") creators: List<Int>
    ): DataWrapper<Story>

    @GET("/v1/public/stories/{storyId}")
    suspend fun getStory(@Path("storyId") storyId: Int): DataWrapper<Story>
}