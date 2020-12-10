package com.example.marvelworld.comicdetails.respository

import com.example.marvelworld.api.models.DataWrapper
import com.example.marvelworld.characterlist.models.Character
import com.example.marvelworld.comiclist.models.Comic
import com.example.marvelworld.creatorlist.models.Creator
import com.example.marvelworld.eventlist.models.Event
import com.example.marvelworld.storylist.models.Story
import retrofit2.http.GET
import retrofit2.http.Path

interface ComicDetailsEndpoint {
    @GET("/v1/public/comics/{comicId}")
    suspend fun getComic(@Path("comicId") comicId: Int): DataWrapper<Comic>

    @GET("/v1/public/comics/{comicId}/characters")
    suspend fun getComicCharacters(@Path("comicId") comicId: Int): DataWrapper<Character>

    @GET("/v1/public/comics/{comicId}/creators")
    suspend fun getComicCreators(@Path("comicId") comicId: Int): DataWrapper<Creator>

    @GET("/v1/public/comics/{comicId}/events")
    suspend fun getComicEvents(@Path("comicId") comicId: Int): DataWrapper<Event>

    @GET("/v1/public/comics/{comicId}/stories")
    suspend fun getComicStories(@Path("comicId") comicId: Int): DataWrapper<Story>
}