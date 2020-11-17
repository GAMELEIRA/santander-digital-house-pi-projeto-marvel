package com.example.marvelworld.characterlist.repository

import com.example.marvelworld.api.utils.NetworkUtils

class CharacterRepository {
    private val characterService = NetworkUtils.getApiService(CharacterEndpoint::class.java)

    suspend fun getCharacters() = characterService.getCharacters()
    suspend fun getCharacter(characterId: Int) = characterService.getCharacter(characterId)
    suspend fun getCharacterComics(characterId: Int) = characterService.getCharacterComics(characterId)
    suspend fun getCharacterEvents(characterId: Int) = characterService.getCharacterEvents(characterId)
    suspend fun getCharacterSeries(characterId: Int) = characterService.getCharacterSeries(characterId)
    suspend fun getCharacterStories(characterId: Int) = characterService.getCharacterStories(characterId)
}
