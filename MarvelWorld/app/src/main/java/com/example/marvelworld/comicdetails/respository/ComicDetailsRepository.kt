package com.example.marvelworld.comicdetails.respository

import com.example.marvelworld.api.utils.NetworkUtils

class ComicDetailsRepository {
    private val comicService = NetworkUtils.getApiService(ComicDetailsEndpoint::class.java)

    suspend fun getComic(comicId: Int) = comicService.getComic(comicId)

    suspend fun getComicCharacters(offset: Int, limit: Int, comicId: Int) =
        comicService.getComicCharacters(comicId, offset, limit)

    suspend fun getComicCreators(offset: Int, limit: Int, comicId: Int) =
        comicService.getComicCreators(comicId, offset, limit)

    suspend fun getComicEvents(offset: Int, limit: Int, comicId: Int) =
        comicService.getComicEvents(comicId, offset, limit)

    suspend fun getComicStories(offset: Int, limit: Int, comicId: Int) =
        comicService.getComicStories(comicId, offset, limit)
}