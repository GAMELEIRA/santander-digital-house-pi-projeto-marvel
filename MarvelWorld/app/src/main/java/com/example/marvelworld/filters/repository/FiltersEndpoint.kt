package com.example.marvelworld.filters.repository

import com.example.marvelworld.api.models.DataWrapper
import com.example.marvelworld.characterlist.models.Character
import retrofit2.http.GET
import retrofit2.http.Query

interface FiltersEndpoint {
    @GET("/v1/public/characters")
    suspend fun getCharacters(@Query("nameStartsWith") nameStartsWith: String): DataWrapper<Character>
}