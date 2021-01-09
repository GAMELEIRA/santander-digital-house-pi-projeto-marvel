package com.example.marvelworld.storylist.repository

import com.example.marvelworld.api.utils.NetworkUtils

class StoryRepository {
    private val storyService = NetworkUtils.getApiService(StoryEndpoint::class.java)

    suspend fun getStories(
        offset: Int,
        limit: Int,
        characters: List<Int>,
        comics: List<Int>,
        events: List<Int>,
        series: List<Int>,
        creators: List<Int>
    ) = storyService.getStories(
        offset,
        limit,
        characters,
        comics,
        events,
        series,
        creators
    )
}