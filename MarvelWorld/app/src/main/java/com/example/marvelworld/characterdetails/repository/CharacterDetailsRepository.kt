package com.example.marvelworld.characterdetails.repository

import com.example.marvelworld.api.utils.NetworkUtils

class CharacterDetailsRepository {
    private val characterService = NetworkUtils.getApiService(CharacterDetailsEndpoint::class.java)
    
    suspend fun getCharacter(characterId: Int) = characterService.getCharacter(characterId)
    suspend fun getCharacterComics(characterId: Int) = characterService.getCharacterComics(characterId)
    suspend fun getCharacterStories(characterId: Int) = characterService.getCharacterStories(characterId)
    suspend fun getCharacterEvents(characterId: Int) = characterService.getCharacterEvents(characterId)
    suspend fun getCharacterSeries(characterId: Int) = characterService.getCharacterSeries(characterId)
}