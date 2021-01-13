package com.example.marvelworld.characterlist.repository

import com.example.marvelworld.api.utils.NetworkUtils

class CharacterRepository {
    private val characterService = NetworkUtils.getApiService(CharacterEndpoint::class.java)

    suspend fun getCharacters(
        offset: Int,
        limit: Int,
        nameStartsWith: String?,
        comics: List<Int>,
        series: List<Int>,
        events: List<Int>
    ) = characterService.getCharacters(offset, limit, nameStartsWith, comics, series, events)

    suspend fun getCharacter(characterId: Int) = characterService.getCharacter(characterId)
}
