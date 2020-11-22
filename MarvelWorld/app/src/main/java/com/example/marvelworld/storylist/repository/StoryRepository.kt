package com.example.marvelworld.storylist.repository

import com.example.marvelworld.api.utils.NetworkUtils

class StoryRepository {
    private val storyService = NetworkUtils.getApiService(StoryEndpoint::class.java)

    suspend fun getStories() = storyService.getStories()
}