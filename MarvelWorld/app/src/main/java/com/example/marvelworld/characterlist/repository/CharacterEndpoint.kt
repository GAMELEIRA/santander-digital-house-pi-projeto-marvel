package com.example.marvelworld.characterlist.repository

import com.example.marvelworld.api.models.DataWrapper
import com.example.marvelworld.characterlist.models.Character
import retrofit2.http.GET
import retrofit2.http.Path

interface CharacterEndpoint {
    @GET("/v1/public/characters")
    suspend fun getCharacters(): DataWrapper<Character>

    @GET("/v1/public/characters/{characterId}")
    suspend fun getCharacter(@Path("characterId") characterId: Int): DataWrapper<Character>

    @GET("/v1/public/characters/{characterId}/comics")
    suspend fun getCharacterComics(@Path("characterId") characterId: Int): DataWrapper<Character>

    @GET("/v1/public/characters/{characterId}/events")
    suspend fun getCharacterEvents(@Path("characterId") characterId: Int): DataWrapper<Character>

    @GET("/v1/public/characters/{characterId}/series")
    suspend fun getCharacterSeries(@Path("characterId") characterId: Int): DataWrapper<Character>

    @GET("/v1/public/characters/{characterId}/stories")
    suspend fun getCharacterStories(@Path("characterId") characterId: Int): DataWrapper<Character>
}