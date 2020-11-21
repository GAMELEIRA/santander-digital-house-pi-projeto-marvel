package com.example.marvelworld.storylist.repository

import com.example.marvelworld.api.models.DataWrapper
import com.example.marvelworld.storylist.models.Story
import retrofit2.http.GET
import retrofit2.http.Path

interface StoryEndpoint {
    @GET("/v1/public/stories")
    suspend fun getStories(): DataWrapper<Story>

    @GET("/v1/public/stories/{storyId}")
    suspend fun getStory(@Path("storyId") storyId: Int): DataWrapper<Story>

    @GET("/v1/public/stories/{storyId}/characters")
    suspend fun getStoryCharacters(@Path("storyId") storyId: Int): DataWrapper<Story>

    @GET("/v1/public/stories/{storyId}/comics")
    suspend fun getStoryComics(@Path("storyId") storyId: Int): DataWrapper<Story>

    @GET("/v1/public/stories/{storyId}/creators")
    suspend fun getStoryCreators(@Path("storyId") storyId: Int): DataWrapper<Story>

    @GET("/v1/public/stories/{storyId}/events")
    suspend fun getStoryEvents(@Path("storyId") storyId: Int): DataWrapper<Story>

    @GET("/v1/public/stories/{storyId}/series")
    suspend fun getStorySeries(@Path("storyId") seriesId: Int): DataWrapper<Story>
}