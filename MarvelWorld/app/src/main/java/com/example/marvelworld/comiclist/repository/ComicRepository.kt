package com.example.marvelworld.comiclist.repository

import com.example.marvelworld.api.utils.NetworkUtils

class ComicRepository {
    private val comicService = NetworkUtils.getApiService(ComicEndpoint::class.java)

    suspend fun getComics(
        offset: Int,
        limit: Int,
        titleStartsWith: String?,
        characters: List<Int>,
        events: List<Int>,
        series: List<Int>,
        creators: List<Int>
    ) = comicService.getComics(offset, limit, titleStartsWith, characters, events, series, creators)

    suspend fun getComic(comicId: Int) = comicService.getComic(comicId)
}