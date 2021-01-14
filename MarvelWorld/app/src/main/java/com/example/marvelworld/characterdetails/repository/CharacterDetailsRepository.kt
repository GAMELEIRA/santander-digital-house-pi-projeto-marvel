package com.example.marvelworld.characterdetails.repository

import com.example.marvelworld.api.utils.NetworkUtils

class CharacterDetailsRepository {
    private val characterService = NetworkUtils.getApiService(CharacterDetailsEndpoint::class.java)

    suspend fun getCharacter(characterId: Int) = characterService.getCharacter(characterId)
    
    suspend fun getCharacterComics(offset: Int, limit: Int, characterId: Int) =
        characterService.getCharacterComics(characterId, offset, limit)

    suspend fun getCharacterStories(offset: Int, limit: Int, characterId: Int) =
        characterService.getCharacterStories(characterId, offset, limit)

    suspend fun getCharacterEvents(offset: Int, limit: Int, characterId: Int) =
        characterService.getCharacterEvents(characterId, offset, limit)

    suspend fun getCharacterSeries(offset: Int, limit: Int, characterId: Int) =
        characterService.getCharacterSeries(characterId, offset, limit)
}