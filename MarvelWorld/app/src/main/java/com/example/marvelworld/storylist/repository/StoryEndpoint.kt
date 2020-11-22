package com.example.marvelworld.storylist.repository

import com.example.marvelworld.api.models.DataWrapper
import com.example.marvelworld.storylist.models.Story
import retrofit2.http.GET

interface StoryEndpoint {
    @GET("/v1/public/stories")
    suspend fun getStories(): DataWrapper<Story>
}