package com.example.marvelworld.characterdetails.repository

import com.example.marvelworld.api.models.DataWrapper
import com.example.marvelworld.characterlist.models.Character
import com.example.marvelworld.comiclist.models.Comic
import com.example.marvelworld.eventlist.models.Event
import com.example.marvelworld.serieslist.models.Series
import com.example.marvelworld.storylist.models.Story
import retrofit2.http.GET
import retrofit2.http.Path

interface CharacterDetailsEndpoint {
    @GET("/v1/public/characters/{characterId}")
    suspend fun getCharacter(@Path("characterId") characterId: Int): DataWrapper<Character>

    @GET("/v1/public/characters/{characterId}/comics")
    suspend fun getCharacterComics(@Path("characterId") characterId: Int): DataWrapper<Comic>

    @GET("/v1/public/characters/{characterId}/stories")
    suspend fun getCharacterStories(@Path("characterId") characterId: Int): DataWrapper<Story>

    @GET("/v1/public/characters/{characterId}/events")
    suspend fun getCharacterEvents(@Path("characterId") characterId: Int): DataWrapper<Event>

    @GET("/v1/public/characters/{characterId}/series")
    suspend fun getCharacterSeries(@Path("characterId") characterId: Int): DataWrapper<Series>
}