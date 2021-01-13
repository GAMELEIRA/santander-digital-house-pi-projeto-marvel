package com.example.marvelworld.characterlist.repository

import com.example.marvelworld.api.models.DataWrapper
import com.example.marvelworld.characterlist.models.Character
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CharacterEndpoint {
    @GET("/v1/public/characters")
    suspend fun getCharacters(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int,
        @Query("nameStartsWith") nameStartsWith: String?,
        @Query("comics") comics: List<Int>,
        @Query("series") series: List<Int>,
        @Query("events") events: List<Int>
    ): DataWrapper<Character>

    @GET("/v1/public/characters/{characterId}")
    suspend fun getCharacter(@Path("characterId") characterId: Int): DataWrapper<Character>
}