package com.example.marvelworld.storylist

import com.example.marvelworld.api.utils.NetworkUtils

class StoryRepository {
    private val storyService = NetworkUtils.getApiService(StoryEndpoint::class.java)

    suspend fun getStories() = storyService.getStories()
    suspend fun getStory(storyId: Int) = storyService.getStory(storyId)
    suspend fun getStoryCharacters(storyId: Int) = storyService.getStoryCharacters(storyId)
    suspend fun getStoryComics(storyId: Int) = storyService.getStoryComics(storyId)
    suspend fun getStoryCreators(storyId: Int) = storyService.getStoryCreators(storyId)
    suspend fun getStoryEvents(storyId: Int) = storyService.getStoryEvents(storyId)
    suspend fun getStorySeries(storyId: Int) = storyService.getStorySeries(storyId)
}