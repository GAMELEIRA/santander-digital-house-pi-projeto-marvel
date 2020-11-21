package com.example.marvelworld.characterlist.repository

import com.example.marvelworld.api.models.DataWrapper
import com.example.marvelworld.characterlist.models.Character
import retrofit2.http.GET

interface CharacterEndpoint {
    @GET("/v1/public/characters")
    suspend fun getCharacters(): DataWrapper<Character>
}