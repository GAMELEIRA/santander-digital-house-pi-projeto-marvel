package com.example.marvelworld.storydetails.repository

import com.example.marvelworld.api.utils.NetworkUtils

class StoryDetailsRepository {
    private val storyService = NetworkUtils.getApiService(StoryDetailsEndpoint::class.java)

    suspend fun getStory(storyId: Int) = storyService.getStory(storyId)

    suspend fun getStoryCharacters(offset: Int, limit: Int, storyId: Int) =
        storyService.getStoryCharacters(storyId, offset, limit)

    suspend fun getStoryComics(offset: Int, limit: Int, storyId: Int) =
        storyService.getStoryComics(storyId, offset, limit)

    suspend fun getStoryCreators(offset: Int, limit: Int, storyId: Int) =
        storyService.getStoryCreators(storyId, offset, limit)

    suspend fun getStoryEvents(offset: Int, limit: Int, storyId: Int) =
        storyService.getStoryEvents(storyId, offset, limit)

    suspend fun getStorySeries(offset: Int, limit: Int, storyId: Int) =
        storyService.getStorySeries(storyId, offset, limit)
}