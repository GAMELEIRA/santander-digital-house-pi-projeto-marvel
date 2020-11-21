package com.example.marvelworld.characterlist.repository

import com.example.marvelworld.api.utils.NetworkUtils

class CharacterRepository {
    private val characterService = NetworkUtils.getApiService(CharacterEndpoint::class.java)

    suspend fun getCharacters() = characterService.getCharacters()
}
