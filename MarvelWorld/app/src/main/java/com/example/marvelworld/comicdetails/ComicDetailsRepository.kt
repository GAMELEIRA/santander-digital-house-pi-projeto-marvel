package com.example.marvelworld.comicdetails

import com.example.marvelworld.api.utils.NetworkUtils

class ComicDetailsRepository {
    private val comicService = NetworkUtils.getApiService(ComicDetailsEndpoint::class.java)

    suspend fun getComic(comicId: Int) = comicService.getComic(comicId)
    suspend fun getComicCharacters(comicId: Int) = comicService.getComicCharacters(comicId)
    suspend fun getComicCreators(comicId: Int) = comicService.getComicCreators(comicId)
    suspend fun getComicEvents(comicId: Int) = comicService.getComicEvents(comicId)
    suspend fun getComicStories(comicId: Int) = comicService.getComicStories(comicId)
}