package com.example.marvelworld.comiclist.repository

import com.example.marvelworld.api.utils.NetworkUtils

class ComicRepository {
    private val comicService = NetworkUtils.getApiService(ComicEndpoint::class.java)

    suspend fun getComics() = comicService.getComics()
}