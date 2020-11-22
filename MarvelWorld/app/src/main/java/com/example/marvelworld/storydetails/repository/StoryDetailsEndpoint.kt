package com.example.marvelworld.storydetails.repository

import com.example.marvelworld.api.models.DataWrapper
import com.example.marvelworld.characterlist.models.Character
import com.example.marvelworld.comiclist.models.Comic
import com.example.marvelworld.creatorlist.models.Creator
import com.example.marvelworld.eventlist.models.Event
import com.example.marvelworld.serieslist.models.Series
import com.example.marvelworld.storylist.models.Story
import retrofit2.http.GET
import retrofit2.http.Path

interface StoryDetailsEndpoint {
    @GET("/v1/public/stories/{storyId}")
    suspend fun getStory(@Path("storyId") storyId: Int): DataWrapper<Story>

    @GET("/v1/public/stories/{storyId}/characters")
    suspend fun getStoryCharacters(@Path("storyId") storyId: Int): DataWrapper<Character>

    @GET("/v1/public/stories/{storyId}/comics")
    suspend fun getStoryComics(@Path("storyId") storyId: Int): DataWrapper<Comic>

    @GET("/v1/public/stories/{storyId}/creators")
    suspend fun getStoryCreators(@Path("storyId") storyId: Int): DataWrapper<Creator>

    @GET("/v1/public/stories/{storyId}/events")
    suspend fun getStoryEvents(@Path("storyId") storyId: Int): DataWrapper<Event>

    @GET("/v1/public/stories/{storyId}/series")
    suspend fun getStorySeries(@Path("storyId") seriesId: Int): DataWrapper<Series>
}