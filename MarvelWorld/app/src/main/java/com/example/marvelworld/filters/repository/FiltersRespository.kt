package com.example.marvelworld.filters.repository

import com.example.marvelworld.api.utils.NetworkUtils

class FiltersRepository {
    private val characterService = NetworkUtils.getApiService(FiltersEndpoint::class.java)

    suspend fun getCharacters(name: String) = characterService.getCharacters(name)
}